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
$(document).ready(function () {
    $('.nav-link2').click(function (e) {
        e.preventDefault();
        $(this).next('.nav-treeview').slideToggle();
        $(this).find('.right').toggleClass('fa-angle-down fa-angle-left');
    });
});
document.addEventListener('DOMContentLoaded', function () {
    var rows = document.getElementsByClassName('clickable-row');
    for (var i = 0; i < rows.length; i++) {
        rows[i].addEventListener('click', function () {
            var action = this.getAttribute('data-action');

            if (action) {
                window.location.href = action;
            }
        });
    }
});
document.addEventListener("DOMContentLoaded", function () {
    flatpickr("#daterange", {
        mode: "range",
        dateFormat: "Y-m-d",
        onClose: function (selectedDates, dateStr, instance) {
        }
    });
});
// document.querySelector('#fileInput').addEventListener('change', (event) => {
//     const file = event.target.files[0];
//     const reader = new FileReader();
//     reader.onload = () => {
//         document.querySelector('#selectedImage').setAttribute('src', reader.result);
//     };
//     reader.readAsDataURL(file);
// });

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

$(document).ready(function () {
    $('.select2-example').select2();
});

function addBlockForAdditionalService() {
    var blocksContainer = document.getElementById("blocks-container");
    var newBlock = document.createElement("div");
    newBlock.className = "row";
    newBlock.innerHTML = `
    <div class="row">
        <div class="col-4" style="margin-top: 30px">
            Услуга
            <input name="services[${blockCounter}].name" style="width: 85%;" type="text" class="form-control">
            <br/>
            Показывать в счетчиках
            <input style="margin-left: 10px" name="services[${blockCounter}].showInCounter" type="checkbox" class="form-check-input" id="flexCheckDefault">
        </div>
        <div class="col-4" style="margin-top: 30px; margin-left: 1%;">
            Ед. изм.
            <div class="input-group">
                <select class="form-select" name="services[${blockCounter}].unitOfMeasurementName" style="width: 50%;">
                    <option>Вибрати</option>
                    ${units.map(function (unit) {
        return `<option value="${unit.name}">${unit.name}</option>`;
    }).join('')}
                </select>
                <span class="input-group-text">               
                    <a type="button" onclick="removeBlock1(this)"><i class="fa fa-trash"></i></a>        
                </span>
            </div>
            <br/>
        </div>
    </div>
`;
    blocksContainer.insertBefore(newBlock, blocksContainer.lastElementChild);
    console.log(blockCounter);
    blockCounter++;
}

function removeBlock1(element) {
    var parentDiv = element.closest('.row');
    parentDiv.remove();
}

function addBlockForSection() {
    var newBlock = document.createElement('div');
    newBlock.className = 'row';
    newBlock.innerHTML = `
    <br/>
    <h7>Название</h7>
    <div class="form-row" style="width: 100%; margin-bottom: 20px; margin-left: 1px;">
      <input name="sections[${indexForSection}]" type="text" class="form-control" style="width: 40%" value="Секция ${indexForSection + 1}">
      <a type="button" class="btn btn-danger form-row-remove-btn" onclick="removeBlock(this)"><i class="fa fa-trash"></i></a>
    </div>
  `;

    var addButton = document.querySelector('#tab_sections .btn-success');
    addButton.parentNode.insertBefore(newBlock, addButton);
    indexForSection++;
}

function addBlockForFloor() {
    var newBlock = document.createElement('div');
    newBlock.className = 'row';
    newBlock.innerHTML = `
    <br/>
    <h7>Название</h7>
    <div class="form-row" style="width: 100%; margin-bottom: 20px; margin-left: 1px;">
      <input name="floors[${indexForFloor}]" type="text" class="form-control" style="width: 40%" value="Етаж ${indexForFloor + 1}">
      <a type="button" class="btn btn-danger form-row-remove-btn" onclick="removeBlock(this)"><i class="fa fa-trash"></i></a>
    </div>
  `;

    var addButton = document.querySelector('#tab_floors .btn-success');
    addButton.parentNode.insertBefore(newBlock, addButton);
    indexForFloor++;
}

function addBlockForUsers() {
    var newBlock = document.createElement('div');
    newBlock.className = 'row';
    var selectOptions = '';
    for (var i = 0; i < users.length; i++) {
        var user = users[i];
        console.log(user.firstName);
        selectOptions += `<option value="${user.firstName}">${user.firstName}</option>`;
    }
    indexForUsers++;
    newBlock.innerHTML = `
    <div class="col-4">
      <select class="form-select" style="width: 100%" aria-label="Default select example" onchange="updateInputValue(this)">
        <option></option>
        ${selectOptions}
      </select>
    </div>
    <div class="col-8">
      <div class="form-row" style="width: 100%; margin-bottom: 20px;">
        <input name="users[${indexForUsers}]" type="text" class="form-control" style="width: 30%" readonly>
        <a type="button" class="btn btn-danger form-row-remove-btn" onclick="removeBlock(this.parentNode)"><i class="fa fa-trash"></i></a>
      </div>
    </div>
  `;
    var addButton = document.querySelector('#tab_users .btn-success');
    addButton.parentNode.insertBefore(newBlock, addButton);
}


function updateInputValue(selectElement) {
    var selectedOption = selectElement.options[selectElement.selectedIndex].value;
    var inputValue = '';
    for (var i = 0; i < users.length; i++) {
        var user = users[i];
        if (user.firstName === selectedOption) {
            inputValue = user.role.name.replace('ROLE_', '');
            break;
        }
    }
    selectElement.parentNode.nextElementSibling.querySelector('input').value = inputValue;
}

function removeBlock(element) {
    var block = element.parentNode.parentNode;
    block.parentNode.removeChild(block);
}

function addBlockForUnitOfMeasurement() {
    var newBlock = document.createElement('div');
    newBlock.className = 'form-row';
    newBlock.innerHTML =
        '<br/>' +
        '<div class="form-row" style="width: 100%; margin-bottom: 20px;">' +
        '   <input name="unitOfMeasurementNames[' + unitsCounter + '].name" type="text" class="form-control" style="width: 40%">' +
        '   <a type="button" class="btn btn-danger form-row-remove-btn" onclick="removeBlock(this)"><i class="fa fa-trash"></i></a>' +
        '</div>';
    var addButton = document.querySelector('#tab_serviceunit .btn-outline-secondary');
    unitsCounter++;
    addButton.parentNode.insertBefore(newBlock, addButton);
}
function addBlockForTariff() {
    var newBlock = document.createElement('div');
    newBlock.className = 'form-row';
    console.log(services);
    newBlock.innerHTML = `
    <div class="row" style="margin-bottom: 20px; width: 100%">
      <div class="col" style="margin-left: 8px;">
        Услуга
        <select class="form-select" name="names[`+indexForTariff+`]" style="width: 90%" onchange="updateUnit(this)">
          <option>Вибрати...</option>
          ${services.map(item => `<option data-unit-of-measurement="${item.unitOfMeasurementName}">${item.name}</option>`).join('')}
        </select>
      </div>
      <div class="col">
        Цена
        <input type="number" name="additionalServiceForTariffDTOS[`+indexForTariff+`].price" class="form-control" style="width: 90%">
      </div>
      <div class="col">
        Валюта
        <input type="text" class="form-control" value="грн" style="width: 90%" readonly>
      </div>
      <div class="col-2">
        Ед. изм.
        <input type="text" class="form-control" value="" style="width: 90%" readonly>
      </div>
    </div>
  `;
    indexForTariff++;

    var addButton = document.querySelector('.btn-outline-secondary');
    addButton.parentNode.insertBefore(newBlock, addButton);
}

function updateUnit(selectElement) {
    var selectedOption = selectElement.options[selectElement.selectedIndex];
    var unitOfMeasurementName = selectedOption.getAttribute('data-unit-of-measurement');
    var unitInput = selectElement.parentNode.nextElementSibling.nextElementSibling.nextElementSibling.querySelector('input[type="text"]');
    unitInput.value = unitOfMeasurementName;
}
function addBlock() {
    var container = document.getElementById("container");
    var newRow = document.createElement("div");
    newRow.classList.add("row");
    newRow.style.marginTop = "20px";
    console.log("qwerty")
    newRow.innerHTML = `
            <div class="col-3">
                Услуга
            </div>
            <div class="col-2">
                Расход
            </div>
            <div class="col-2">
                Ед. изм.
            </div>
            <div class="col-2">
                Цена за ед., грн.
            </div>
            <div class="col-3">
                Стоимость, грн.
            </div>
        `;
    container.appendChild(newRow);
}

