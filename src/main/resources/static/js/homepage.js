'use strict';

window.onload = function() {

    var $svgObject = $('#js-svg-object');
    var $svgDocument = $svgObject[0].contentDocument;

    var $alertModal = $('#js-modal-alert');
    var $alertTitle = $('#js-modal-alert-title');
    var $alertMessage = $('#js-modal-alert-message');

    var $producerBox = $('#svg-producer-box', $svgDocument);
    var $producerModal = $('#js-modal-producer');
    var $producerForm = $('#js-modal-producer-form');

    var $productsTopicBox = $('#svg-products-topic-box', $svgDocument);
    var $membersTopicBox = $('#svg-members-topic-box', $svgDocument);
    var $commercialOrdersTopicBox = $('#svg-commercial-orders-topic-box', $svgDocument);
    var $convertedCommercialOrdersTopicBox = $('#svg-full-commercial-orders-topic-box', $svgDocument);
    var $splitCommercialOrderLinesTopicBox = $('#svg-commercial-order-lines-topic-box', $svgDocument);
    var $purchaseOrdersTopicBox = $('#svg-purchase-orders-topic-box', $svgDocument);
    var $purchaseOrderLinesTopicBox = $('#svg-purchase-order-lines-topic-box', $svgDocument);

    var $commercialOrderLinesSplitStreamBox = $('#svg-stream-commercial-order-lines-split-box', $svgDocument);
    var $purchaseOrderLinesGenerateStreamBox = $('#svg-stream-purchase-order-lines-generate-box', $svgDocument);

    var $topicContentModal = $('#js-modal-topics-content');
    var $topicContentTitle = $('#js-modal-topics-content-title');
    var $topicContentBody = $('#js-modal-topics-content-body');
    var $topicContentTable = $('#js-modal-topics-content-table');
    var $topicContentTotal = $('#js-modal-topics-content-total');
    var $topicContentButtonFirst = $('#js-modal-topics-content-first');
    var $topicContentButtonPrev = $('#js-modal-topics-content-prev');
    var $topicContentButtonNext = $('#js-modal-topics-content-next');

    var $streamToggler = $('a[id^="svg-"][id$="-toggle"]', $svgDocument);

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
            dataType: 'json',
            type: $form.attr('method'),
            url: $form.attr('action'),
            success: function (response) {
                setTimeout(countTopics(), 500);
                showMessage('Created ' + response.length + ' commercial order(s) with random data!');
            },
            error: function (xhr) {
                var defaultMsg = 'An error occurred creating commercial orders!';
                var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                showError(msg);
            }
        });
    });

    $productsTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getProductsUrl');
        loadTopicContent(url);
    });

    $membersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getMembersUrl');
        loadTopicContent(url);
    });

    $commercialOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getCommercialOrdersUrl');
        loadTopicContent(url);
    });

    $convertedCommercialOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getConvertedCommercialOrdersUrl');
        loadTopicContent(url);
    });

    $splitCommercialOrderLinesTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getSplitCommercialOrderLinesUrl');
        loadTopicContent(url);
    });

    $purchaseOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getPurchaseOrdersUrl');
        loadTopicContent(url);
    });

    $purchaseOrderLinesTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('getPurchaseOrderLinesUrl');
        loadTopicContent(url);
    });

    $purchaseOrderLinesGenerateStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('showPurchaseOrderLinesDetailsUrl');
        loadTopicContent(url);
    });

    $commercialOrderLinesSplitStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $topicContentModal.data('showCommercialOrderLinesDetailsUrl');
        loadTopicContent(url);
    });

    $streamToggler.click(function (ev) {
        ev.preventDefault();
        var id = $(this).attr('id');
        var prodid = '&' + id.slice(4, -7);
        var url = $svgObject.data('processToggleUrl').replace("{procid}", prodid);
        $.post(url)
            .done(function () {
                checkProcessesStatuses();
            })
            .fail(function (xhr) {
                var defaultMsg = 'An error occurred changing the status of a process!';
                var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                showError(msg);
            });
    });

    var showMessage = function (text) {
        $alertTitle.addClass('text-primary').removeClass('text-danger');
        $alertTitle.html('Message');
        $alertMessage.addClass('alert-primary').removeClass('alert-danger');
        $alertMessage.html(text);
        $alertModal.modal('show');
    };

    var showError = function (text) {
        $alertTitle.addClass('text-danger').removeClass('text-primary');
        $alertTitle.html('Error');
        $alertMessage.addClass('alert-danger').removeClass('alert-primary');
        $alertMessage.html(text);
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
                var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                console.error(msg);
            });
    };

    var checkProcessesStatuses = function() {
        $.get($svgObject.data('processesStatusUrl'))
            .done(function (response) {
                if (typeof response !== 'undefined' && response != null) {
                    for (var process in response) {
                        // Remove the initial "&" from the process bean name
                        var selector = '#svg-' + process.substr(1) + '-text';
                        var status = response[process];
                        $(selector, $svgDocument).html(status ? '&#9658;' : '&#9726;');
                    }
                }
            })
            .fail(function (xhr) {
                var defaultMsg = 'An error occurred retrieving the status of the Kafka Streams processes!';
                var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                console.error(msg);
            });
    };

    var loadTopicContent = function(url) {
        if (url === "" || url == null || typeof url == 'undefined') {
            showError('Can\'t get data from the server. Invalid URL!');
        }
        else {
            $.get(url)
                .done(function (response) {
                    var $title = $('#js-modal-topics-content-title', response);
                    $topicContentTitle.html($title.text());

                    var $error = $('#js-modal-topics-content-error', response);
                    if ($error.length > 0) {
                        var alert = '<div class="alert alert-danger" role="alert">' + $error.html() + '</div>';
                        $topicContentBody.html(alert);
                        $topicContentTotal.html('');
                        $topicContentButtonFirst.hide();
                        $topicContentButtonPrev.hide();
                        $topicContentButtonNext.hide();
                    } else {
                        var $body = $('#js-modal-topics-content-body', response);
                        if ($body.length > 0) {
                            $topicContentBody.html($body.html());
                            $topicContentTotal.html('');
                            $topicContentButtonFirst.hide();
                            $topicContentButtonPrev.hide();
                            $topicContentButtonNext.hide();
                        } else {
                            var $table = $('#js-modal-topics-content-table', response);
                            var $total = $('#js-modal-topics-content-total', response);
                            $topicContentBody.html($table);
                            $topicContentTotal.replaceWith($total);
                            $topicContentTable = $('#js-modal-topics-content-table');
                            $topicContentTotal = $('#js-modal-topics-content-total');

                            var $targetLink = $('#js-modal-topics-content-first', response);
                            setButtonClickHandler($topicContentButtonFirst, $targetLink.attr('href'));

                            $targetLink = $('#js-modal-topics-content-prev', response);
                            setButtonClickHandler($topicContentButtonPrev, $targetLink.attr('href'));

                            $targetLink = $('#js-modal-topics-content-next', response);
                            setButtonClickHandler($topicContentButtonNext, $targetLink.attr('href'));
                        }
                    }

                    $topicContentModal.modal('show');
                })
                .fail(function (xhr) {
                    var defaultMsg = 'An error occurred getting data from the server!';
                    var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                    showError(msg);
                });
        }
    };

    var setButtonClickHandler = function($button, url) {
        $button.unbind('click');
        if (typeof url != 'undefined' && url !== '') {
            $button.prop('disabled', false);
            $button.click(function (ev) {
                ev.preventDefault();
                loadTopicContent(url);
            });
        } else {
            $button.prop('disabled', true);
        }
        $button.show();
    };

    countTopics();
    checkProcessesStatuses();
};
