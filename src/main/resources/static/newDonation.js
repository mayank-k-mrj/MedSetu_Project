let medName = document.querySelector('#med_name');
let medExp = document.querySelector('#med_exp');
let medBatch = document.querySelector('#med_batch');
let medImg = document.querySelector('#fileInput');

const urlMain = "/medicine";
const urlMed = "/upload";
const urlUpd = "/updatemed";

let urlId = null;

const form = document.querySelector('#donationForm');

function updateFileName() {
    const fileName = document.getElementById('fileName');

    if (medImg.files.length > 0) {
        fileName.textContent = medImg.files[0].name;
        fileName.style.color = "#2ecc71";
    }
}

let updatedImageUrl;

medImg.addEventListener("input", async function(e) {
    e.preventDefault();

    if (!medImg.files.length) {
        alert("Please select image");
        return;
    }

    const formData = new FormData();
    formData.append("file", medImg.files[0]);

    try {
        const response = await fetch(urlMain + urlMed, {
            method: "POST",
            body: formData
        });

        let gotRes = await response.json();

        if (response.ok) {
            console.log("Successfully uploaded!");
            console.log(gotRes);
            autoFill(gotRes);
            updatedImageUrl = gotRes.imageUrl;
        } else {
            console.log("Failed uploading!");
        }

    } catch (error) {
        console.log("Error:", error);
    }
});

function autoFill(gotRes){
    medName.value = gotRes.name;
    medExp.value = gotRes.expiryDate;
    medBatch.value = gotRes.batchNumber;
    urlId = gotRes.id;
    console.log("Filled");
}

async function saveDetails(e){
    e.preventDefault();
    if(medName.value != "" && medExp.value != "" && medBatch.value != ""){
        try{
            const finalDetails = {
                    name: medName.value,
                    expiryDate: medExp.value,
                    batchNumber: medBatch.value,
                    imageUrl: updatedImageUrl
                }
                let medUpdUrl = urlMain + "/" + urlId + urlUpd;
                let response = await fetch(medUpdUrl, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body : JSON.stringify(finalDetails)
                });
                if(response.ok){
                    console.log("Updated data saved successfully!");
                    window.location.href = "userDashboard.html";
                }
                else{
                    console.log("Something went wrong while sending data");
                    alert("First fill the User Address page");
                    window.location.href = "user_regi.html";
                }
        }
        catch(error){
             console.log("Error : ", error);
        }

    }
}

form.addEventListener("submit", saveDetails);
