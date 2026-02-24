let fullname = document.querySelector('#fullname');
let email = document.querySelector('#email');
let contact = document.querySelector('#phone');
let userPassword = document.querySelector('#password');
let submit = document.querySelector('.login-btn');

const form = document.querySelector("form");

let cards = document.querySelectorAll('.role-card');

let urlMain = "/users";
let urlActive = "/activeuser";
let urlReg = "/register";
let urlProf = "/profile";

cards.forEach(card => {
    card.addEventListener('click', function(e){
        e.preventDefault();
        let cardValue = e.currentTarget.dataset.role;
        localStorage.setItem("cardValue", cardValue);
        console.log()
        window.location.href = "signup.html";
    })
})

async function userRegistration(event){
    let roleCard = localStorage.getItem("cardValue");
    if(fullname != null && email != null && contact != null && userPassword != null && roleCard != null){
    event.preventDefault();
        const Data = {
            name: fullname.value,
            username: email.value,
            phone: contact.value,
            password: userPassword.value,
            role: roleCard
        }

        let Response = await fetch(urlMain + urlReg, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(Data)
        });
        if(Response.ok){
            alert("Account Created!");
            console.log("Data saved successfully.");
            window.location.href = "/login";
        }
        else{
            console.log("Data not saved something went wrong.");
        }
    }
    else{
        alert("All fields required");
    }
}

form.addEventListener("submit", userRegistration);