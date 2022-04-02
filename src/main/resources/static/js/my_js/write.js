
function setPreviewImage(files){
    console.log(files);
    let container = document.getElementById("image_container");
    container.innerHTML = "";

    let fileArr = Array.from(files);

    fileArr.forEach( file => {
        const reader = new FileReader();
        let divTag = document.createElement("div");
        let imgTag = document.createElement("img");
        imgTag.classList.add("preview-img");
        divTag.appendChild(imgTag);

        reader.onload = ev => {
            imgTag.src = ev.target.result;
        }

        reader.readAsDataURL(file);
        container.appendChild(divTag);
    });

}