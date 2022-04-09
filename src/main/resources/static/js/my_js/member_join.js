// 아이디 중복 체크
function duplicateCheck(){

    let idField = document.getElementById('id');
    let modalIdField = document.getElementById('input_id');

    if(modalIdField.value.length > 30 || modalIdField.value.trim() == ''){
        console.log('modal value: ' + modalIdField.value.length);
        alert("30자 이내로 입력해주세요");
        return;
    }

    $.ajax({
        url: "/checkUniqueId",
        data: { inputId: modalIdField.value },
        type: "GET",
        dataType: "text",

        success: function(result){
            console.log(result);
            if(result == 'unique'){
                alert("사용가능한 아이디입니다.")
                $('#idcheckModal').modal('hide');
                idField.value = modalIdField.value;
            } else{
                alert("사용할수 없는 아이디입니다.")
            }
        },
        error: function (error){
            console.log('error');
        }
    });

}

function checkForm(){

    if(idVal == ''){
        alert("ID 중복체크를 해주세요");
        return false;
    }
    return true;
}
