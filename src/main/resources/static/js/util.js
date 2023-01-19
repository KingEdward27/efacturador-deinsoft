$(document).ready(function () {
    var a = $('<!--[if IE]><link rel="shortcut Icon"  type="image/X-icon" href="../../images/favicon.png" /><![endif]--><![if !IE]><link rel="shortcut Icon" type="image/png"  href="../../images/favicon.png" /><![endif]>');
    $("head").append(a);
    $('#loading').hide();
    $.ajaxSetup({cache: false});
});
function callConfirmDialog(mensaje, funcion) {
    //    var patron = /\s+/g;//REGX para saber si lo que trae tiene espacios
    //    var val = patron.test(mensaje);
    //    if (!val) {
    //        if (mensaje !== null || mensaje !== "") {
    //            mensaje = getMessage(mensaje);
    //        }
    //    }
    $("#msgConfirm").html(mensaje);
    $("#dlgConfirm").dialog({
        modal: true,
        width: 300,
        dialogClass: 'dlgfixed',
        position: "center",
        resizable: false,
        buttons: {
            "SI": function () {
                $(this).dialog("close");
                funcion();
            },
            "NO": function () {
                $(this).dialog("close");
                return 0;
            }
        }
    });
}