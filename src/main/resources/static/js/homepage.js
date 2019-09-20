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
    var $generatedWarehouseOrderLinesTopic = $('#svg-warehouse-order-lines-topic-box', $svgDocument);
    var $matchedWarehouseOrderLinesTopic = $('#svg-matched-warehouse-order-lines-topic-box', $svgDocument);
    var $unmatchedWarehouseOrderLinesTopic = $('#svg-unmatched-warehouse-order-lines-topic-box', $svgDocument);
    var $recoveredWarehouseOrderLinesTopic = $('#svg-recovered-warehouse-order-lines-topic-box', $svgDocument);
    var $failedWarehouseOrderLinesTopic = $('#svg-failed-warehouse-order-lines-topic-box', $svgDocument);
    var $fullWarehouseOrderLinesTopic = $('#svg-full-warehouse-order-lines-topic-box', $svgDocument);
    var $productsCacheTopic = $('#svg-products-cache-topic-box', $svgDocument);

    var $commercialOrderConverterStreamBox = $('#svg-stream-commercial-orders-converter-box', $svgDocument);
    var $commercialOrderLinesSplitStreamBox = $('#svg-stream-commercial-order-lines-split-box', $svgDocument);
    var $purchaseOrderLinesGenerateStreamBox = $('#svg-stream-purchase-order-lines-generate-box', $svgDocument);
    var $purchaseOrdersGenerateStreamBox = $('#svg-stream-purchase-order-generate-box', $svgDocument);
    var $warehouseOrderLinesGenerateStreamBox = $('#svg-stream-warehouse-order-line-generator-box', $svgDocument);
    var $warehouseOrderLinesMatcherStreamBox = $('#svg-match-with-legacy-product-id-box', $svgDocument);
    var $warehouseOrderLinesRecoverStreamBox = $('#svg-recover-warehouse-order-lines-box', $svgDocument);
    var $warehouseOrderLinesMergerStreamBox = $('#svg-merge-warehouse-order-lines-box', $svgDocument);
    var $warehouseOrdersGeneratorStreamBox = $('#svg-generate-warehouse-orders-box', $svgDocument);
    var $productLegacyIdFeederStreamBox = $('#svg-product-legacy-id-feeder-box', $svgDocument);

    var $contentModal = $('#js-modal-content');
    var $contentTitle = $('#js-modal-content-title');
    var $contentBody = $('#js-modal-content-body');
    var $contentTable = $('#js-modal-content-table');
    var $contentTotal = $('#js-modal-content-total');
    var $contentButtonFirst = $('#js-modal-content-first');
    var $contentButtonPrev = $('#js-modal-content-prev');
    var $contentButtonNext = $('#js-modal-content-next');

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
                startCountTopicsTimers();
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
        var url = $contentModal.attr('data-get-products-url');
        loadTopicContent(url);
    });

    $membersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-members-url');
        loadTopicContent(url);
    });

    $commercialOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-commercial-orders-url');
        loadTopicContent(url);
    });

    $convertedCommercialOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-converted-commercial-orders-url');
        loadTopicContent(url);
    });

    $splitCommercialOrderLinesTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-split-commercial-order-lines-url');
        loadTopicContent(url);
    });

    $purchaseOrdersTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-purchase-orders-url');
        loadTopicContent(url);
    });

    $purchaseOrderLinesTopicBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-purchase-order-lines-url');
        loadTopicContent(url);
    });

    $generatedWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-generated-url');
        loadTopicContent(url);
    });

    $matchedWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-matched-url');
        loadTopicContent(url);
    });

    $unmatchedWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-unmatched-url');
        loadTopicContent(url);
    });

    $recoveredWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-recovered-url');
        loadTopicContent(url);
    });

    $failedWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-failed-url');
        loadTopicContent(url);
    });

    $fullWarehouseOrderLinesTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-warehouse-order-lines-full-url');
        loadTopicContent(url);
    });

    $productsCacheTopic.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-get-products-cache-url');
        loadTopicContent(url);
    });

    $commercialOrderConverterStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-commercial-orders-converter-details-url');
        loadTopicContent(url);
    });

    $commercialOrderLinesSplitStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-commercial-order-lines-details-url');
        loadTopicContent(url);
    });

    $purchaseOrderLinesGenerateStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-purchase-order-lines-details-url');
        loadTopicContent(url);
    });

    $purchaseOrdersGenerateStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-purchase-orders-details-url');
        loadTopicContent(url);
    });

    $warehouseOrderLinesGenerateStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-warehouse-order-lines-details-url');
        loadTopicContent(url);
    });

    $warehouseOrderLinesMatcherStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-warehouse-order-lines-match-details-url');
        loadTopicContent(url);
    });

    $warehouseOrderLinesRecoverStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-warehouse-order-lines-recover-details-url');
        loadTopicContent(url);
    });

    $warehouseOrderLinesMergerStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-warehouse-order-lines-merger-details-url');
        loadTopicContent(url);
    });

    $warehouseOrdersGeneratorStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-warehouse-orders-details-url');
        loadTopicContent(url);
    });

    $productLegacyIdFeederStreamBox.click(function (ev) {
        ev.preventDefault();
        var url = $contentModal.attr('data-show-product-legacy-id-feeder-details-url');
        loadTopicContent(url);
    });

    $streamToggler.click(function (ev) {
        ev.preventDefault();
        var id = $(this).attr('id');
        var prodid = '&' + id.slice(4, -7);
        var url = $svgObject.attr('data-process-toggle-url').replace("{procid}", prodid);
        $.post(url)
            .done(function () {
                checkProcessesStatuses();
                startCountTopicsTimers();
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

    var startCountTopicsTimers = function() {
        countTopics();
        setTimeout(countTopics, 500);
        setTimeout(countTopics, 1000);
        setTimeout(countTopics, 2500);
        setTimeout(countTopics, 5000);
        setTimeout(countTopics, 10000);
        setTimeout(countTopics, 25000);
    };

    var countTopics = function() {
        $.get($svgObject.attr('data-topics-count-url'))
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
        $.get($svgObject.attr('data-processes-status-url'))
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
                    var $title = $('#js-modal-content-title', response);
                    $contentTitle.html($title.text());

                    var $error = $('#js-modal-content-error', response);
                    if ($error.length > 0) {
                        var alert = '<div class="alert alert-danger" role="alert">' + $error.html() + '</div>';
                        $contentBody.html(alert);
                        $contentTotal.html('');
                        $contentButtonFirst.hide();
                        $contentButtonPrev.hide();
                        $contentButtonNext.hide();
                    } else {
                        var $body = $('#js-modal-content-body', response);
                        if ($body.length > 0) {
                            $contentBody.html($body.html());
                            $contentTotal.html('');
                            $contentButtonFirst.hide();
                            $contentButtonPrev.hide();
                            $contentButtonNext.hide();
                        } else {
                            var $table = $('#js-modal-content-table', response);
                            var $total = $('#js-modal-content-total', response);
                            $contentBody.html($table);
                            $contentTotal.replaceWith($total);
                            $contentTable = $('#js-modal-content-table');
                            $contentTotal = $('#js-modal-content-total');

                            var $targetLink = $('#js-modal-content-first', response);
                            setButtonClickHandler($contentButtonFirst, $targetLink.attr('href'));

                            $targetLink = $('#js-modal-content-prev', response);
                            setButtonClickHandler($contentButtonPrev, $targetLink.attr('href'));

                            $targetLink = $('#js-modal-content-next', response);
                            setButtonClickHandler($contentButtonNext, $targetLink.attr('href'));
                        }

                        $('a', $contentBody).click(function (ev) {
                            ev.preventDefault();
                            var url = $(this).attr('href');
                            loadTopicContent(url);
                        });

                        $('form', $contentBody).submit(function (ev) {
                            ev.preventDefault();
                            var $form = $(this);
                            $.ajax({
                                data: $form.serialize(),
                                dataType: 'html',
                                type: $form.attr('method'),
                                url: $form.attr('action'),
                                success: function (response) {
                                    startCountTopicsTimers();
                                    showMessage('Form successfully submitted!');
                                },
                                error: function (xhr) {
                                    var defaultMsg = 'An error occurred submitting the form!';
                                    var msg = JSON.parse(xhr.responseText || '{}').message || defaultMsg;
                                    showError(msg);
                                }
                            });

                            $contentModal.modal('hide');
                        });
                    }

                    $contentModal.modal('show');
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
