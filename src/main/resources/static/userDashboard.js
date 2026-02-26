let tableBody = document.querySelector('#medicineTableBody');
let user_name = document.querySelector('#user_name');

// 1. User Profile Fetch
async function getUserProf() {
    try {
        const response = await fetch("/users/activeuser");
        if (response.ok) {
            let user = await response.json();
            user_name.textContent = user.name;
            if (!user.profilecomplete) {
                window.location.href = "user_regi.html";
            }
        }
    } catch (error) { console.error("Profile Error:", error); }
}

// 2. Fetch Medicines & Offers
async function fetchAllMedicines() {
    try {
        const response = await fetch("/medicine/fetchall");
        if (!response.ok) return;

        const medicines = await response.json();
        tableBody.innerHTML = "";

        for (let med of medicines.reverse()) {
            let offersHtml = "";

            try {
                const offRes = await fetch(`/offers/${med.id}/alloffers`);
                if (offRes.ok) {
                    const offers = await offRes.json();

                    offers.forEach(offer => {
                        // FIX: Latest price nikalne ke liye logic (Bargain price pehle check karo)
                        const latestPrice = offer.counter_price ? offer.counter_price : offer.offered_price;

                        if (offer.status === 'ACCEPTED') {
                            offersHtml += `<div class="offer-row status-accepted">Final Deal: ₹${latestPrice}</div>`;
                        } else if (offer.status === 'REJECTED') {
                            offersHtml += `<div class="offer-row status-rejected">Rejected: ₹${latestPrice}</div>`;
                        } else {
                            // Buttons for Pending/Countered Offers
                            offersHtml += `
                                <div class="offer-card">
                                    <p>Current Offer: <strong>₹${latestPrice}</strong></p>
                                    <div class="offer-btns">
                                        <button class="u-btn accept" onclick="acceptOffer(${offer.id})">Accept</button>
                                        <button class="u-btn bargain" onclick="openBargain(${offer.id})">Bargain</button>
                                        <button class="u-btn reject" onclick="rejectOffer(${offer.id})">Reject</button>
                                    </div>
                                </div>`;
                        }
                    });
                }
            } catch (e) { console.log("Offers error for med:", med.id); }

            const row = document.createElement("tr");
            row.innerHTML = `
                <td><strong>${med.name}</strong></td>
                <td>${med.quantity || "0"} Units</td>
                <td>${med.expiryDate || "-"}</td>
                <td>
                    <span class="badge ${med.status.toLowerCase()}">${med.status}</span>
                    <div class="offers-list">${offersHtml}</div>
                </td>
            `;
            tableBody.appendChild(row);
        }
        updateStats();
    } catch (error) { console.error("Fetch Error:", error); }
}

// 3. Accept Logic (Donor Side)
async function acceptOffer(offerId) {
    if (!confirm("NGO ka price accept karein? Deal finalize ho jayegi.")) return;
    try {
        const response = await fetch(`/offers/${offerId}/accept`);
        if (response.ok) {
            alert("Deal Finalized!");
            fetchAllMedicines();
        } else {
            alert("Accept failed. Status: " + response.status);
        }
    } catch (e) { console.error(e); }
}

// 4. Bargain Logic (Redirect to page)
async function openBargain(offerId) {
    window.location.href = `counter.html?offerid=${offerId}`;
}

// 5. Reject Logic
async function rejectOffer(offerId) {
    if (!confirm("Offer reject karein?")) return;
    try {
        const response = await fetch(`/offers/${offerId}/reject`);
        if (response.ok) {
            alert("Offer Rejected.");
            fetchAllMedicines();
        }
    } catch (e) { console.error(e); }
}

// 6. Stats Update
async function updateStats() {
    try {
        const res = await fetch("/medicine/fetchall");
        const meds = await res.json();
        document.querySelector('#pending_count').textContent = meds.filter(m => m.status === 'AVAILABLE').length;
    } catch (e) { }
}

document.querySelector('#donation').addEventListener('click', () => { window.location.href = "newDonation.html"; });
document.addEventListener("DOMContentLoaded", () => { getUserProf(); fetchAllMedicines(); });

// ==========================================================
// NEW CODE FOR TABS AND HISTORY (Appended safely)
// ==========================================================

// Function to switch between tabs
function switchUserTab(tabId, element) {
    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(sec => sec.style.display = 'none');

    // Remove active class from all nav items
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(nav => nav.classList.remove('active'));

    // Show the targeted section
    document.getElementById(tabId).style.display = 'block';

    // Highlight the clicked sidebar option
    if (element) {
        element.classList.add('active');
    }

    // Load data for specific tabs
    if (tabId === 'tab-donations' || tabId === 'tab-history') {
        loadOtherTabsData();
    }
}

// Function to load data for 'My Donations' and 'History'
async function loadOtherTabsData() {
    try {
        const response = await fetch("/medicine/fetchall");
        if (!response.ok) return;

        const medicines = await response.json();

        const donationsTbody = document.getElementById('donationsTableBody');
        const historyTbody = document.getElementById('historyTableBody');

        if(donationsTbody) donationsTbody.innerHTML = "";
        if(historyTbody) historyTbody.innerHTML = "";

        // Overview Stats update karne ke liye
        let totalCount = medicines.length;
        let pendingCount = 0;
        let acceptedCount = 0;

        for (let med of medicines.reverse()) {

            // Stats calculation
            if(med.status === 'AVAILABLE' || med.status === 'PENDING') pendingCount++;
            if(med.status === 'SOLD' || med.status === 'COMPLETED') acceptedCount++;

            // Normal Row HTML (bina buttons ke, kyunki history hai)
            const rowHtml = `
                <tr>
                    <td><strong>${med.name}</strong></td>
                    <td>${med.quantity || "0"} Units</td>
                    <td>${med.expiryDate || "-"}</td>
                    <td><span class="badge ${med.status.toLowerCase()}">${med.status}</span></td>
                </tr>
            `;

            // Agar bik gayi hai ya process khatam, toh HISTORY mein dalo
            if (med.status === 'SOLD' || med.status === 'COMPLETED' || med.status === 'REJECTED') {
                if(historyTbody) historyTbody.innerHTML += rowHtml;
            }
            // Warna ACTIVE DONATIONS mein dalo
            else {
                if(donationsTbody) donationsTbody.innerHTML += rowHtml;
            }
        }

        // Stats UI update
        const statTotal = document.getElementById('stat-total');
        const statAccepted = document.getElementById('stat-accepted');
        if(statTotal) statTotal.textContent = totalCount;
        if(statAccepted) statAccepted.textContent = acceptedCount;

    } catch (error) {
        console.error("Error loading other tabs:", error);
    }
}