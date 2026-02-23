let urlAPI = "https://api.postalpincode.in/pincode/";

let urlMain = "/ngo";
let urlReg = "/ngoregister";

let NGO_name = document.querySelector('#ngoname');
let NGO_address = document.querySelector('#address');
let NGO_city = document.querySelector('#city');
let NGO_pincode = document.querySelector('#pincode');
let NGO_licenseNo = document.querySelector('#lisNo');

let form = document.querySelector('form');

let currentPinCode;

function getInput(){
    currentPinCode = NGO_pincode.value;
    if(currentPinCode.length == 6){
        findRegion(currentPinCode);
    }
}

async function findRegion(pin){
    let Location = await fetch(urlAPI + pin);
    let data = await Location.json();
    if(Location.ok){
        let post = data[0].PostOffice[0];
        let district = post.District;
        console.log("Location found successfully.");
        console.log("District : ",district);
        NGO_city.value = district;
    }
    else{
        console.log("Something went wrong with api.");
    }
}

NGO_pincode.addEventListener('input', getInput);

async function saveInfo(event){
    event.preventDefault();
    const ngo_fields = {
        ngoname: NGO_name.value,
        address: NGO_address.value,
        city: NGO_city.value,
        pincode: NGO_pincode.value,
        licenseno: NGO_licenseNo.value
    }
    let Response = await fetch(urlMain + urlReg, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ngo_fields)
    });
    if(Response.ok){
        console.log("NGO data successfully saved.");
        window.location.href = "ngoDashboard.html";
    }
    else{
        console.log("Data not saved something went wrong.");
    }
}

form.addEventListener("submit", saveInfo);