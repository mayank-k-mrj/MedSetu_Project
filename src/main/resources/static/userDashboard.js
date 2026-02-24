let tableBody = document.querySelector('#medicineTableBody');
let user_name = document.querySelector('#user_name');
let donationId;

async function getUserProf(){
    let profileStat;

    const Response = await fetch("/users/activeuser");

    if (Response.ok){
        let user = await Response.json();
        profileStat = user.profilecomplete;
        user_name.textContent = user.name;
    } else {
        console.log("Something went wrong!");
    }

    if(!profileStat){
        setTimeout(() => {
            window.location.href = "user_regi.html";
        }, 500);
    }
}

window.addEventListener("DOMContentLoaded", getUserProf);

let donationBtn = document.querySelector('#donation');

donationBtn.addEventListener('click', () => {
    window.location.href = "newDonation.html";
});

async function fetchAllMedicines(){
    try{
        const response = await fetch("/medicine/fetchall");

        if (!response.ok){
            throw new Error("Failed to fetch data");
        }

        const medicines = await response.json();
        console.log("Data fetched!", medicines);

        tableBody.innerHTML = "";

        const lastFour = medicines.slice(-4);

        for (let med of lastFour) {

            const res2 = await fetch("/medicine/" + med.id + "/fetchone");

            if(res2.ok){
                donationId = await res2.json();
            }

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${med.name}</td>
                <td>${med.batchNumber}</td>
                <td>${med.expiryDate}</td>
                <td>
                    <span class="badge ${donationId?.status?.toLowerCase()}">
                        ${donationId?.status}
                    </span>
                </td>
            `;

            tableBody.appendChild(row);
        }

        filterPending();

    } catch(error){
        console.log("Error:", error);
    }
}

document.addEventListener("DOMContentLoaded", fetchAllMedicines);

async function filterPending(){
    let pending_block = document.querySelector('#pending_count');
    const response = await fetch("/medicine/fetchallStats");
    if(!response.ok){
        console.log("Error occurred while fetching all stats");
    }
    const donation = await response.json();

    let pending = donation.filter(don => {
        return don.status === "PENDING"
    });

    pending_block.textContent = pending.length;
}