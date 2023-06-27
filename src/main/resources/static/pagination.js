var scriptElement = document.querySelector('script[src="/pagination.js"]');
var url = scriptElement.getAttribute('data-url');
var currentPage = scriptElement.getAttribute('current');
var totalButtons = scriptElement.getAttribute('max');
var pagination = document.getElementById('pagination-example');

function updatePagination() {
    pagination.innerHTML = '';
    var li, link;
    if (totalButtons <= 7) {
        for (var i = 1; i <= totalButtons; i++) {
            li = createPaginationItem(i);
            pagination.appendChild(li);
        }
    } else {
        li = createPaginationItem(1);
        pagination.appendChild(li);
        if (currentPage <= 4) {
            for (var i = 2; i <= 5; i++) {
                li = createPaginationItem(i);
                pagination.appendChild(li);
            }
            li = createEllipsisItem();
            pagination.appendChild(li);
        } else if (parseInt(currentPage) >= parseInt(totalButtons) - 3) {

            li = createEllipsisItem();
            pagination.appendChild(li);
            for (var i = totalButtons-4; i <= totalButtons-1; i++) {
                li = createPaginationItem(i);
                pagination.appendChild(li);
            }
        } else {
            li = createEllipsisItem();
            pagination.appendChild(li);

            for (var i = currentPage - 1; i <= parseInt(currentPage) + 1; i++) {
                li = createPaginationItem(i);
                pagination.appendChild(li);
            }
            li = createEllipsisItem();
            pagination.appendChild(li);
        }
        li = createPaginationItem(totalButtons);
        pagination.appendChild(li);
    }
}

function createPaginationItem(pageNumber) {
    var li = document.createElement('li');
    li.classList.add('page-item');
    if (parseInt(pageNumber) === parseInt(currentPage)) {
        li.classList.add('active');
    }
    var link = document.createElement('a');
    if(window.location.href.includes('filter')) {
        var parts = window.location.href.split('?');
        link.href = pageNumber+'?'+parts[1];
    }
    else link.href = pageNumber;
    link.classList.add('page-link');
    link.innerText = pageNumber;
    li.appendChild(link);

    return li;
}


function createEllipsisItem() {
    var li = document.createElement('li');
    li.innerText = '...';
    li.classList.add('page-item');
    li.style.margin = '0 5px';
    li.style.fontWeight = 'bold';
    li.style.fontSize = '16px';
    return li;
}

updatePagination();
