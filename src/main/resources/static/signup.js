let fullname = document.querySelector('#fullname');
let email = document.querySelector('#email');
let contact = document.querySelector('#phone');
let selectedRole = document.querySelector('#role');
let userPassword = document.querySelector('#password');
let submit = document.querySelector('.login-btn');

const form = document.querySelector("form");

let urlMain = "/users";
let urlActive = "/activeuser";
let urlReg = "/register";
let urlProf = "/profile";

//let active_user;

//Fetching user for further uses

//getUser();
//
//async function getUser(){
//    const Response = await fetch(urlMain + urlActive);
//    if (Response.ok){
//        let user = await Response.json();
//        console.log(user);
//        active_user = user.username;
//        console.log(active_user);
//    }
//    else{
//    console.log("Something went wrong!");
//    }
//}


async function userRegistration(event){
    if(fullname != null && email != null && contact != null && userPassword != null && selectedRole != null){
    event.preventDefault();
        const Data = {
            name: fullname.value,
            username: email.value,
            phone: contact.value,
            password: userPassword.value,
            role: selectedRole.value
        }

        let Response = await fetch(urlMain + urlReg, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(Data)
        });
        if(Response.ok){
            console.log("Data saved successfully.");
            window.open("/login");
        }
        else{
            console.log("Data not saved something went wrong.");
        }
    }
}

form.addEventListener("submit", userRegistration);