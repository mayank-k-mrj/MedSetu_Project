let urlAPI = "https://api.postalpincode.in/pincode/";

const form = document.getElementById('userForm');
const btn = document.getElementById('submitBtn');
const btnText = btn.querySelector('.btn-text');
let user_city = document.querySelector('#city');
let user_address = document.querySelector('#address');
let user_pincode = document.querySelector('#pincode');

//form.addEventListener('submit', function (e) {
//
//
//    // Start Animation
//
//
//    // Simulate a network delay (2 seconds)
//
//
//        // Optional: Reset button after 3 seconds
//
//
//    }, 500);
//});

let currentPinCode;

function getInput() {
    currentPinCode = user_pincode.value;
    if (currentPinCode.length == 6) {
        findRegion(currentPinCode);
        console.log(currentPinCode);
    }
}

async function findRegion(pin) {
    let Location = await fetch(urlAPI + pin);
    let data = await Location.json();
    if (Location.ok) {
        let post = data[0].PostOffice[0];
        let district = post.District;
        console.log("Location found successfully.");
        console.log("District : ", district);
        city.value = district;
    }
    else {
        console.log("Something went wrong with api.");
    }
}

user_pincode.addEventListener('input', getInput);


async function saveDetails(e){
    e.preventDefault();

    btn.classList.add('loading');

    let parsePincode = Number(user_pincode.value);

    if(user_address.value && user_city.value && user_pincode.value){

        let params = {
            pincode: parsePincode,
            city: user_city.value,
            address: user_address.value
        };

        try {
            let response = await fetch("/users/profile", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(params)
            });

            if(response.ok){
                btn.classList.remove('loading');
                btn.classList.add('success');
                btnText.innerHTML = "✓ Updated Successfully!";

                setTimeout(() => {
                    btn.classList.remove('success');
                    btnText.innerHTML = "Saved";
                    form.reset();
                    window.location.href = "userDashboard.html";
                }, 1000);
            }
            else{
                console.log("Error occurred while sending data!");
            }

        } catch(error){
            console.log("Network error:", error);
        }
    }
}

form.addEventListener("submit", saveDetails);