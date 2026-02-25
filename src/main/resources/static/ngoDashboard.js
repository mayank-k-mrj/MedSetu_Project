let activeUserId = null;
let activeNgoId = null;

// 1. Tab Switching Logic
function switchTab(tabId, element) {
    document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    if(element) element.classList.add('active');
}

// 2. Fetch Logged-in User Profile
async function getUserProf() {
    try {
        const response = await fetch("/users/activeuser");
        if (response.ok) {
            let user = await response.json();
            activeUserId = user.id;
            if (!user.profilecomplete) {
                window.location.href = "ngo_regi.html";
            } else {
                fetchNgoDetails(user.id);
            }
        } else {
            window.location.href = "login.html";
        }
    } catch (error) { console.error(error); }
}

// 3. Fetch NGO Details
async function fetchNgoDetails(userId) {
    try {
        const res = await fetch(`/ngo/${userId}/getngo`);
        if (res.ok) {
            const ngoData = await res.json();
            activeNgoId = ngoData.id;
            document.getElementById('header-ngo-name').innerText = (ngoData.ngoname || "NGO") + " Dashboard";
            document.getElementById('sidebar-ngo-name').innerText = ngoData.ngoname || "MedSetu NGO";
            loadDonations();
        }
    } catch (err) { console.error(err); }
}

// 4. Main Table Logic
async function loadDonations() {
    if (!activeNgoId) return;

    const pendingTbody = document.getElementById("pending-tbody");
    const inventoryTbody = document.getElementById("inventory-tbody");

    try {
        const response = await fetch("/ngo/alldonations");
        if (!response.ok) return;

        const donations = await response.json();
        if(pendingTbody) pendingTbody.innerHTML = "";
        if(inventoryTbody) inventoryTbody.innerHTML = "";

        let pendingCount = 0;
        let acceptedStock = 0;
        let completedCount = 0;

        for (let donation of donations) {
            const med = donation.medicine;

            // --- TAB 1: PENDING DONATIONS ---
            if (donation.status === 'PENDING' && pendingTbody) {
                pendingCount++;
                let offerActionsHtml = `<button class="action-btn" style="background-color:#007bff; color:white;" onclick="goToOfferPage(${med.id}, '${med.name}', ${med.quantity})"><i class="fas fa-plus"></i> Create Offer</button>`;

                try {
                    const offerRes = await fetch(`/offers/${med.id}/alloffers`);
                    if (offerRes.ok) {
                        const offers = await offerRes.json();
                        const myOffer = offers.find(o => (o.ngoId == activeNgoId) || (o.ngo && o.ngo.id == activeNgoId));

                        if (myOffer) {
                            if (myOffer.status === 'ACCEPTED') {
                                offerActionsHtml = `<span style="color:green; font-weight:bold;">✅ Deal Done</span>`;
                            } else if (myOffer.status === 'REJECTED') {
                                offerActionsHtml = `<span style="color:red; font-weight:bold;">❌ Rejected</span>`;
                            } else if (myOffer.status === 'COUNTERED') {
                                // NGO TABHI ACCEPT KAR SAKTA HAI JAB USER NE COUNTER KIYA HO
                                offerActionsHtml = `
                                    <div style="display:flex; gap:5px;">
                                        <button class="action-btn btn-warning" onclick="goToCounterPage(${myOffer.id})">Bargain</button>
                                        <button class="action-btn btn-success" style="background-color:#28a745;" onclick="ngoAcceptDeal(${myOffer.id})">Accept Deal</button>
                                        <button class="action-btn btn-danger" onclick="rejectOfferInDb(${myOffer.id})"><i class="fas fa-trash"></i></button>
                                    </div>`;
                            } else {
                                // PENDING STATE
                                offerActionsHtml = `
                                    <div style="display:flex; gap:5px; align-items:center;">
                                        <span style="font-size:12px; color:#666;">Waiting...</span>
                                        <button class="action-btn btn-danger" onclick="rejectOfferInDb(${myOffer.id})"><i class="fas fa-trash"></i></button>
                                    </div>`;
                            }
                        }
                    }
                } catch (err) { console.error(err); }

                pendingTbody.innerHTML += `
                    <tr>
                        <td><strong>${donation.donor?.name || "Donor"}</strong></td>
                        <td>${med.name}</td>
                        <td>${med.quantity} units</td>
                        <td>${med.expiryDate || "N/A"}</td>
                        <td><a href="${med.imageUrl || '#'}" target="_blank">View</a></td>
                        <td>${offerActionsHtml}</td>
                    </tr>`;

            // --- TAB 2: INVENTORY (ACCEPTED/SOLD ITEMS) ---
            } else if (donation.status === 'ACCEPTED' && inventoryTbody) {
                acceptedStock += med.quantity;
                inventoryTbody.innerHTML += `
                    <tr>
                        <td>${med.name}</td><td>${med.quantity} units</td><td>${med.expiryDate}</td>
                        <td><span style="color: #28a745; font-weight:bold;"><i class="fas fa-box-open"></i> In Stock</span></td>
                        <td><button class="action-btn btn-info" onclick="updateDonationStatus(${donation.id}, 'COMPLETED')"><i class="fas fa-truck"></i> Mark Distributed</button></td>
                    </tr>`;
            } else if (donation.status === 'COMPLETED') {
                completedCount++;
            }
        }

        // Stats Update
        if(document.getElementById('stat-pending')) document.getElementById('stat-pending').innerText = pendingCount;
        if(document.getElementById('stat-stock')) document.getElementById('stat-stock').innerText = acceptedStock;
        if(document.getElementById('stat-distributions')) document.getElementById('stat-distributions').innerText = completedCount;

    } catch (error) { console.error("Fetch Error:", error); }
}

// 5. API Functions
window.ngoAcceptDeal = async (offerId) => {
    if (!confirm("Finalize this deal?")) return;
    try {
        const response = await fetch(`/offers/${offerId}/ngo-accept`, { method: 'PUT' });
        if (response.ok) { alert("Deal Successful! Medicine status: SOLD."); loadDonations(); }
    } catch (error) { console.error(error); }
};

window.rejectOfferInDb = async (offerId) => {
    if (!confirm("Cancel this offer?")) return;
    try {
        const response = await fetch(`/offers/${offerId}/reject`);
        if (response.ok) { alert("Offer Cancelled."); loadDonations(); }
    } catch (error) { console.error(error); }
};

async function updateDonationStatus(id, newStatus) {
    if (!confirm(`Mark as ${newStatus}?`)) return;
    try {
        const response = await fetch(`/ngo/${id}/statupdate?status=${newStatus}`, { method: 'PUT' });
        if (response.ok) loadDonations();
    } catch (error) { console.error(error); }
}

// 6. Redirects
window.goToOfferPage = (medId, medName, qty) => {
    window.location.href = `offer.html?medid=${medId}&ngoid=${activeNgoId}&name=${encodeURIComponent(medName)}&qty=${qty}`;
}
window.goToCounterPage = (offerId) => {
    window.location.href = `counter.html?offerid=${offerId}`;
}

// Init
document.addEventListener('DOMContentLoaded', () => {
    getUserProf();
    switchTab('verificationsTab');
});