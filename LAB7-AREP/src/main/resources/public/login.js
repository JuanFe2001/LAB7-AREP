function loadGetMsg() {
    let nameVar = document.getElementById("name").value;
    let passwordVar = document.getElementById("password").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        const result = JSON.parse(this.responseText).result;
        let response = "Acceso Incorrecto";
        if (result == true) {
            response = "Acceso Exitoso";
        }
        document.getElementById("getrespmsg").innerHTML = response;
    }
    xhttp.open("GET", "/login?name=" + nameVar + "&password=" + passwordVar);
    xhttp.send();
}