$(document).ready(function() {
    const logoImage = $('.logo-image');
    const miniLogoImage = $('.mini-logo-image');

    if ($('#sidebar').hasClass('sidebar-collapse')) {
        logoImage.css('display', 'block');
        miniLogoImage.css('display', 'none');
    } else {
        logoImage.css('display', 'none');
        miniLogoImage.css('display', 'block');
    }

    $('[data-widget="pushmenu"]').click(function() {
        if ($('body').hasClass('sidebar-collapse')) {
            logoImage.css('display', 'block');
            miniLogoImage.css('display', 'none');
        } else {
            logoImage.css('display', 'none');
            miniLogoImage.css('display', 'block');
        }
    });

    var table = $('#myTable');
    var rowsPerPage = 1;
    var currentPage = 0;
    var maxButtons = 10;

    function showPage(page) {
        table.find('tbody tr').hide();
        table.find('tbody tr').slice(page * rowsPerPage, (page + 1) * rowsPerPage).show();
    }

    function updatePagination() {
        var totalPages = Math.ceil(table.find('tbody tr').length / rowsPerPage);
        var nextPage = currentPage + 1;
        var prevPage = currentPage - 1;
        var startPage = Math.max(currentPage - Math.floor(maxButtons / 2), 0);
        var endPage = Math.min(startPage + maxButtons - 1, totalPages - 1);

        $('#pagination').empty();
        if (totalPages <= 1) {
            return;
        }

        var prevButton = $('<li class="page-item"><a id="prevPage" class="page-link" href="#">«</a></li>');
        if (currentPage === 0) {
            prevButton.addClass('disabled');
        }
        prevButton.on('click', function(e) {
            e.preventDefault();
            if (currentPage - 1 >= 0) {
                currentPage--;
                showPage(currentPage);
                updatePagination();
            }
        });
        $('#pagination').append(prevButton);

        // Додаємо номеровані кнопки пагінації
        for (var i = startPage; i <= endPage; i++) {
            var pageButton = $('<li class="page-item"><a class="page-link" href="#">' + (i + 1) + '</a></li>');
            if (i === currentPage) {
                pageButton.addClass('active');
            }
            pageButton.on('click', function(e) {
                e.preventDefault();
                var pageNumber = parseInt($(this).text()) - 1;
                currentPage = pageNumber;
                showPage(currentPage);
                updatePagination();
            });
            $('#pagination').append(pageButton);
        }

        // Додаємо кнопку "Наступна"
        var nextButton = $('<li class="page-item"><a id="nextPage" class="page-link" href="#">»</a></li>');
        if ((currentPage + 1) === totalPages) {
            nextButton.addClass('disabled');
        }
        nextButton.on('click', function(e) {
            e.preventDefault();
            if (currentPage + 1 < totalPages) {
                currentPage++;
                showPage(currentPage);
                updatePagination();
            }
        });
        $('#pagination').append(nextButton);
    }

    showPage(currentPage);
    updatePagination();
});