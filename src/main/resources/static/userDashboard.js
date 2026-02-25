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

        // Nayi medicines upar dikhane ke liye reverse kiya
        for (let med of medicines.reverse()) {
            let offersHtml = "";

            try {
                const offRes = await fetch(`/offers/${med.id}/alloffers`);
                if (offRes.ok) {
                    const offers = await offRes.json();

                    offers.forEach(offer => {
                        if (offer.status === 'ACCEPTED') {
                            offersHtml += `<div class="offer-row status-accepted">Final Deal: ₹${offer.offered_price}</div>`;
                        } else if (offer.status === 'REJECTED') {
                            offersHtml += `<div class="offer-row status-rejected">Rejected: ₹${offer.offered_price}</div>`;
                        } else {
                            // Buttons for Pending/Countered Offers
                            offersHtml += `
                                <div class="offer-card">
                                    <p>Offer from NGO: <strong>₹${offer.offered_price}</strong></p>
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
                <td><strong>${med.name}</strong><br><small>Qty: ${med.quantity}</small></td>
                <td>${med.batchNo || "N/A"}</td>
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

async function openBargain(offerId) {
    const newPrice = prompt("Apna price (₹) enter karein:");

    // Agar user cancel kar de ya khali chhode
    if (newPrice === null || newPrice.trim() === "" || isNaN(newPrice)) {
        return;
    }

    try {
        const response = await fetch(`/offers/${offerId}/counter`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json' // Batana padega ki hum JSON bhej rahe hain
            },
            // Backend @RequestBody mang raha hai, toh JSON.stringify zaroori hai
            body: JSON.stringify({
                counterPrice: parseFloat(newPrice)
            })
        });

        if (response.ok) {
            alert("Bargain price bhej diya gaya!");
            fetchAllMedicines(); // List refresh karo
        } else {
            const errorText = await response.text();
            alert("Error: " + errorText);
        }
    } catch (e) {
        console.error("Bargain Fetch Error:", e);
        alert("Server se connection nahi ho paya.");
    }
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