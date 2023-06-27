$(document).ready(function () {
    const logoImage = $('.logo-image');
    const miniLogoImage = $('.mini-logo-image');

    if ($('#sidebar').hasClass('sidebar-collapse')) {
        logoImage.css('display', 'block');
        miniLogoImage.css('display', 'none');
    } else {
        logoImage.css('display', 'none');
        miniLogoImage.css('display', 'block');
    }

    $('[data-widget="pushmenu"]').click(function () {
        if ($('body').hasClass('sidebar-collapse')) {
            logoImage.css('display', 'block');
            miniLogoImage.css('display', 'none');
        } else {
            logoImage.css('display', 'none');
            miniLogoImage.css('display', 'block');
        }
    });

    var table = $('#myTable');
    var rowsPerPage = 10;
    var currentPage = 0;
    var maxButtons = 5;

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
        prevButton.on('click', function (e) {
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
            pageButton.on('click', function (e) {
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
        nextButton.on('click', function (e) {
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
$(document).ready(function () {
    $('.mySelect').select2();
});
$(document).ready(function() {
    $('.nav-link2').click(function(e) {
        e.preventDefault();
        $(this).next('.nav-treeview').slideToggle();
        $(this).find('.right').toggleClass('fa-angle-down fa-angle-left');
    });
});
document.addEventListener('DOMContentLoaded', function() {
    var rows = document.getElementsByClassName('clickable-row');
    for (var i = 0; i < rows.length; i++) {
        rows[i].addEventListener('click', function() {
            var action = this.getAttribute('data-action');

            if (action) {
                window.location.href = action;
            }
        });
    }
});
document.addEventListener("DOMContentLoaded", function() {
    flatpickr("#daterange", {
        mode: "range",
        dateFormat: "Y-m-d",
        onClose: function(selectedDates, dateStr, instance){}
    });
});
document.querySelector('#fileInput').addEventListener('change', (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
        document.querySelector('#selectedImage').setAttribute('src', reader.result);
    };
    reader.readAsDataURL(file);
});
function generatePassword() {
    var passwordLength = 8;
    var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    var password = "";

    for (var i = 0; i < passwordLength; i++) {
        var randomIndex = Math.floor(Math.random() * characters.length);
        password += characters.charAt(randomIndex);
    }

    document.getElementById("password-input").value = password;
    document.getElementById("confirm-password-input").value = password;
}

function togglePasswordVisibility() {
    var passwordInput = document.getElementById("password-input");
    var confirmPasswordInput = document.getElementById("confirm-password-input");
    var toggleButton = document.getElementById("toggle-button");
    var toggleIcon = document.getElementById("toggle-icon");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        confirmPasswordInput.type = "text";
        toggleIcon.classList.remove("fa-eye-slash");
        toggleIcon.classList.add("fa-eye");
    } else {
        passwordInput.type = "password";
        confirmPasswordInput.type = "password";
        toggleIcon.classList.remove("fa-eye");
        toggleIcon.classList.add("fa-eye-slash");
    }
}
function confirmDelete() {
    return confirm("Вы уверены, что хотите удалить этот элемент?");
}
function submitForm() {
    document.getElementById("formSend").submit();
}
$(document).ready(function() {
    $('.select2-example').select2();
});
