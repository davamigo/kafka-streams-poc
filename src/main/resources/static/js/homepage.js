'use strict';

window.onload = function() {

    var $svgObject = $('#svg-object');
    var $svgDocument = $svgObject[0].contentDocument;

    var $alertModal = $('#svg-alert-modal');
    var $alertTitle = $('#svg-alert-title');
    var $alertText = $('#svg-alert-text');

    var $producerBox = $('#svg-producer-box', $svgDocument);
    var $producerModal = $('#svg-producer-modal');
    var $producerForm = $('#svg-producer-form');

    $producerBox.click(function (ev) {
        ev.preventDefault();
        $producerModal.modal('show');
    });

    $producerForm.submit(function (ev) {
        ev.preventDefault();
        var $form = $(this);
        $producerModal.modal('hide');
        $.ajax({
            data: $form.serialize(),
            dataType: "json",
            type: $form.attr('method'),
            url: $form.attr('action'),
            success: function (response) {
                setTimeout(countTopics(), 500);
                showMessage("Created " + response.length + " commercial order(s) with random data!");
            },
            error: function (xhr) {
                var defaultMsg = 'An error occurred creating commercial orders!';
                var msg = JSON.parse(xhr.responseText || "{}").message || defaultMsg;
                showError(msg);
            }
        });
    });

    var showMessage = function (text) {
        $alertTitle.addClass('text-primary').removeClass('text-danger');
        $alertTitle.html("Message");
        $alertText.addClass('alert-primary').removeClass('alert-danger');
        $alertText.html(text);
        $alertModal.modal('show');
    };

    var showError = function (text) {
        $alertTitle.addClass('text-danger').removeClass('text-primary');
        $alertTitle.html("Error");
        $alertText.addClass('alert-danger').removeClass('alert-primary');
        $alertText.html(text);
        $alertModal.modal('show');
    };

    var countTopics = function() {
        $.get($svgObject.data('topicsCountUrl'))
            .done(function (response) {
                if (typeof response !== 'undefined' && response != null) {
                    for (var topic in response) {
                        $('#svg-' + topic + '-topic-count', $svgDocument).html(response[topic]);
                    }
                }
            })
            .fail(function (xhr) {
                var defaultMsg = 'An error occurred retrieving the records count for the topics!';
                var msg = JSON.parse(xhr.responseText || "{}").message || defaultMsg;
                console.error(msg);
            });
    };

    countTopics();
};
