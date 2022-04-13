//toastr 메시지 띄우기
$(document).ready(function() {

    const state = $('#stateTag').val();
    const message = $('#messageTag').val();

    if(state === 'SUCCESS') {
        console.log("state is true")
        toastr.success(message);

    } else if(state === 'FAILURE'){
        console.log("state is false")
        toastr.error(message);
    }

});