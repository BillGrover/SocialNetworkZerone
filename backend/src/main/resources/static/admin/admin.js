'use strict';
let role = {
        admin: 'Администратор',
        moderator: 'Модератор'
    },
    methods = {
        get: 'GET',
        post: 'POST',
        put: 'PUT',
        delete: 'DELETE'
    },
    popup = {
        alert: 'alert',
        notification: 'notification'
    },
    apiMapping = '/api/v1',
    popupTimeOut = 3000,
    loginPage = document.querySelector('.login'),
    loginForm = loginPage.querySelector('.login-form'),
    adminPanel = document.querySelector('.admin-box'),
    userName,
    userImageUrl,
    userRole,
    title = document.querySelector('.admin-title'),
    titleImage = document.querySelector('.admin-title__img'),
    titleName = title.querySelector('.admin-title__name'),
    titlePosition = title.querySelector('.admin-title__position');

// CHECK
function check(element) {
    return !(typeof element === 'undefined' || element === null || element === '' || element === false);
}

// POPUP
function showAlert(text) {
    console.log('ALERT: ' + text);
    if (!check(text)) text = 'Ошибка не распознана';
    showPopUp(popup.alert, text);
}

function showNotification(text) {
    showPopUp(popup.notification, text);
}

function showPopUp(type, text) {

    let popup = document.createElement('div'),
        wrapper = document.querySelector('.popup__wrapper');
    if (wrapper === null) {
        wrapper = document.createElement('div');
        wrapper.classList.add('popup__wrapper');
        document.body.append(wrapper);
    }
    popup.textContent = text;
    popup.classList.add('popup');
    popup.classList.add(type);
    wrapper.append(popup);
    console.log('POPUP: ' + type + ' ' + text);
    setTimeout(function () {
        hidePopup(popup)
    }, popupTimeOut);
    popup.addEventListener('click', function (e) {
        console.log(e.target);
        e.target.remove();
    })
}

function hidePopup(element) {
    element.remove();
    let wrapper = document.querySelector('.popup__wrapper');
    if (wrapper.children.length === 0) wrapper.remove()
}

// REQUEST
function sendRequest(method, data, link, callback, error) {
    let request = new XMLHttpRequest(),
        response,
        token = localStorage.getItem('adminToken');

    request.open(method, apiMapping + link);
    console.log("Запрос: " + apiMapping + link);
    request.setRequestHeader('Authorization', token);
    request.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
    if (data !== null && data !== '') {
        console.log('DATA');
        console.log(data);
        console.log(typeof data);
        let toJSON = {},
            jsonData;
        if (link === '/admin/login') {
            data.forEach(function (value, key) {
                toJSON[key] = value;
                jsonData = JSON.stringify(toJSON);
            });
        } else {
            jsonData = data;
        }
        request.send(jsonData);
    } else {
        request.send();
    }
    request.onreadystatechange = function () {
        if (request.readyState < 4) {
            console.log("загружается...");
        } else if (request.readyState === 4 && request.status === 200) {
            response = JSON.parse(request.response);
            console.log("Данные получены:");
            console.log(response);
            if (check(callback)) callback(response);
            if (typeof response.data !== 'undefined' && response.data.messageText !== 'undefined') {
                showNotification(response.data.messageText)
            }
        } else {
            console.log(error);
            response = JSON.parse(request.response);
            showAlert(response.statusText);
        }
    }
}

function sendGetRequest(link, callback, error) {
    sendRequest(methods.get, null, link, callback, error);
}

function sendPostRequest(data, link, callback, error) {
    sendRequest(methods.post, data, link, callback, error);
}

function sendDeleteRequest(data, link, callback, error) {
    sendRequest(methods.delete, data, link, callback, error);
}

// MAIN PAGE STATUS UPDATE
function adminOnly() {
    let items = document.querySelectorAll('.adminOnly');
    items.forEach(el => el.remove());
}

// TABS
let pages = document.querySelectorAll('.admin-page'),
    menuItems = document.querySelectorAll('.admin-menu__list-item');

function getMenuItemByClass(text) {
    for (let i = 0; i < menuItems.length; i++) {
        if (menuItems[i].classList.contains(text)) return menuItems[i];
    }
    return menuItems[0];
}

function displayPage(pageName) {
    console.log('Отображаем страницу ' + pageName);
    addPageContent.textContent = '';
    hideAllPages();
    deleteActiveMenuSelector();
    let page = document.querySelector('.' + pageName + '__page'),
        item = getMenuItemByClass(pageName);
    page.style.display = 'block';
    item.classList.add('admin-menu__list-item--active');
    sendGetRequest(pageDataRequests[pageName][0], pageDataRequests[pageName][1], 'Не удачный запрос по странице ' + pageName);
}

function onMenuClick(e) {
    e.preventDefault();
    let pageName = e.target.getAttribute('page');
    if (pageName === null) return;
    console.log('Нажато: ' + pageName);
    localStorage.setItem('adminPage', pageName);
    displayPage(pageName);
}

function hideAllPages() {
    pages.forEach(p => p.style.display = 'none');
}

function deleteActiveMenuSelector() {
    menuItems.forEach(el => el.classList.remove('admin-menu__list-item--active'));
}

menuItems.forEach(el => el.addEventListener('click', onMenuClick));

// FILTERING
function deselectItems(items) {
    items.forEach(el => {
        el.classList.remove('admin-aside__item--active')
    });
}

function itemsFiltering(items, filter, needle) {
    let counter = 0;
    items.forEach(el => {
        let display;
        if ((filter === 'all' || el.getAttribute('gx-attribute') === filter)
            &&
            (el.querySelector('.card__name').textContent.toUpperCase().indexOf(needle.toUpperCase()) > -1)) {
            display = 'flex';
            counter++;
        } else {
            display = 'none';
        }
        el.style.display = display;
    });
    return counter;
}

let timeoutId;

// STATISTICS PAGE
let statisticsPage = pages[0],
    personsTotal = statisticsPage.querySelector('.persons-counter'),
    postsTotal = statisticsPage.querySelector('.posts-counter'),
    commentsTotal = statisticsPage.querySelector('.comments-counter'),
    likesTotal = statisticsPage.querySelector('.likes-counter'),
    statisticsFilters = statisticsPage.querySelectorAll('.admin-aside__item');
statisticsFilters.forEach(el => el.addEventListener('click', function () {
    showAlert('Не реализовано');
}))

function setTotalStatistics(response) {
    console.log("Выполняется setTotalStatistic");
    personsTotal.textContent = response['persons'].toLocaleString();
    postsTotal.textContent = response['posts'].toLocaleString();
    commentsTotal.textContent = response['comments'].toLocaleString();
    likesTotal.textContent = response['likes'].toLocaleString();
}

// PERSONS PAGE
let personsPage = pages[1],
    personsPageContent = personsPage.querySelector('.admin-page__content'),
    personsFilters = personsPage.querySelectorAll('.admin-aside__item'),
    personsItems = personsPage.querySelectorAll('.card'),
    personsSearchInput = personsPage.querySelector('.admin-page__input'),
    personsCounter = personsPage.querySelector('.admin-page__counter'),
    personsAlert = personsPage.querySelector('.admin-page__alert');

function onPersonFilterClick(e) {
    e.preventDefault();
    deselectItems(personsFilters);
    e.target.classList.add('admin-aside__item--active');
    let filter = e.target.getAttribute('gx-filter'),
        text = pages[1].querySelector('.admin-page__input').value;
    let counter = itemsFiltering(personsItems, filter, text)
    personsAlert.style.display = counter === 0 ? 'block' : 'none';
    personsCounter.textContent = '' + counter;
}

personsFilters.forEach(el => el.addEventListener('click', onPersonFilterClick));
personsSearchInput.addEventListener('input', function (e) {
    let filter = pages[1].querySelector('.admin-aside__item--active').getAttribute('gx-filter');
    clearTimeout(timeoutId);
    timeoutId = setTimeout(function () {
        let counter = itemsFiltering(personsItems, filter, e.target.value);
        personsAlert.style.display = counter === 0 ? 'block' : 'none';
        personsCounter.textContent = '' + counter;
    }, 300);
})

// SUPPORT PAGE
let supportsPage = pages[2],
    supportsPageContent = supportsPage.querySelector('.admin-page__content'),
    supportsFilters = supportsPage.querySelectorAll('.admin-aside__item'),
    supportsItems = supportsPage.querySelectorAll('.card'),
    supportsCounter = supportsPage.querySelector('.admin-page__counter'),
    supportsAlert = supportsPage.querySelector('.admin-page__alert');

function onSupportFilterClick(e) {
    e.preventDefault();
    deselectItems(supportsFilters);
    e.target.classList.add('admin-aside__item--active');
    let filter = e.target.getAttribute('gx-filter');
    let counter = itemsFiltering(supportsItems, filter, '');
    supportsAlert.style.display = counter === 0 ? 'block' : 'none';
    supportsCounter.textContent = '' + counter;
}

supportsFilters.forEach(el => el.addEventListener('click', onSupportFilterClick));

// ADMINS PAGE
let adminsPage = pages[3],
    adminsPageContent = adminsPage.querySelector('.admin-page__content'),
    adminsFilters = adminsPage.querySelectorAll('.admin-aside__item'),
    adminsItems = adminsPage.querySelectorAll('.card'),
    adminsSearchInput = adminsPage.querySelector('.admin-page__input'),
    adminsCounter = adminsPage.querySelector('.admin-page__counter'),
    adminsAlert = adminsPage.querySelector('.admin-page__alert');

function onAdminFilterClick(e) {
    e.preventDefault();
    deselectItems(adminsFilters);
    e.target.classList.add('admin-aside__item--active');
    let filter = e.target.getAttribute('gx-filter'),
        text = pages[3].querySelector('.admin-page__input').value;
    let counter = itemsFiltering(adminsItems, filter, text);
    adminsAlert.style.display = counter === 0 ? 'block' : 'none';
    adminsCounter.textContent = '' + counter;
}

adminsFilters.forEach(el => el.addEventListener('click', onAdminFilterClick));
adminsSearchInput.addEventListener('input', function (e) {
    let filter = pages[2].querySelector('.admin-aside__item--active').getAttribute('gx-filter');
    clearTimeout(timeoutId);
    timeoutId = setTimeout(function () {
        let counter = itemsFiltering(adminsItems, filter, e.target.value);
        adminsAlert.style.display = counter === 0 ? 'block' : 'none';
        adminsCounter.textContent = '' + counter;
    }, 300);
})

// ADD PAGE
let addPage = pages[4],
    addPageCloseBtn = createCloseBtn(),
    addPageTitle = document.createElement('div'),
    addPageContent = document.createElement('div');
addPageTitle.classList.add('admin-page__title');
addPageTitle.classList.add('title');
addPageContent.classList.add('add-page__content');
addPage.append(addPageCloseBtn);
addPage.append(addPageTitle);
addPage.append(addPageContent);
addPageCloseBtn.addEventListener('click', function () {
    addPage.style.display = 'none';
    addPageContent.textContent = '';
    displayPage(localStorage.getItem('adminPage'));
})

// PAGE DATA REQUESTS
let pageDataRequests = {
    statistics: ['/admin/statistic/all', setTotalStatistics],
    persons: ['/admin/persons', setPersonCard],
    supports: ['/support/orders', setOrderCard],
    settings: ['/admin/admins', setAdminCard]
}

// LOGIN
function eraseLoginInputs() {
    let inputs = loginForm.querySelectorAll('input');
    inputs[0].value = '';
    inputs[1].value = '';
}

function setUserData(user) {
    userName = user.name;
    userImageUrl = user.imageUrl;
    userRole = user.role === 'ADMIN' ? role.admin : role.moderator;
    if (userRole !== role.admin) adminOnly();
    titleImage.setAttribute('src', userImageUrl);
    titleName.textContent = userName;
    titlePosition.textContent = userRole;
    localStorage.setItem('adminToken', user.token);
    eraseLoginInputs();
    loginPage.style.display = 'none';
    adminPanel.style.display = 'block';
    let page = localStorage.getItem('adminPage');
    if (page != null) {
        displayPage(page)
    } else {
        displayPage('statistics')
    }
}

function getUser() {
    let data = new FormData(loginForm);
    sendPostRequest(data, "/admin/login", setUserData, 'Пользователь не найден')
}

function getUserByToken() {
    sendGetRequest("/admin/login", setUserData, 'Пользователь не найден');
}

// CARD

function createButtonBox() {
    let buttonBox = document.createElement('div');
    buttonBox.classList.add('card__button-box');
    return buttonBox;
}

function createCard(id, image, name, description, buttonBox, label, number) {

    let item = document.createElement('div');
    item.classList.add('card');
    item.setAttribute('id', id);

    let infoBox = document.createElement('div');
    infoBox.classList.add(('card__info-box'));
    if (check(image)) {
        let thisImage = document.createElement('img');
        thisImage.classList.add('card__img');
        thisImage.setAttribute('src', image);
        thisImage.setAttribute('alt', 'Аватар пользователя');
        infoBox.append(thisImage);
    }
    let textBox = document.createElement('div'),
        thisName = document.createElement('p'),
        thisDescription = document.createElement('p');
    textBox.classList.add('card__text-box');
    thisName.classList.add('card__name');
    thisDescription.classList.add('card__description');
    thisName.textContent = name;
    thisDescription.textContent = description;
    textBox.append(thisName);
    textBox.append(thisDescription);
    infoBox.append(textBox);
    item.append(infoBox);

    if (check(buttonBox)) {
        item.append(buttonBox);
    }
    if (check(label)) {
        let thisLabel = document.createElement('div');
        thisLabel.classList.add('card__label');
        thisLabel.textContent = label;
        item.append(thisLabel);
    }
    if (check(number)) {
        let thisNumber = document.createElement('div');
        thisNumber.classList.add('card__number');
        thisNumber.textContent = number;
        item.append(thisNumber);
    }

    return item;
}

function markCard(element, status) {
    element.className = 'card';
    element.classList.add(status);
    let buttonBox = element.querySelector('.card__button-box'),
        label = element.querySelector('.card__label');
    buttonBox.textContent = '';

    switch (status) {
        case 'active':
            buttonBox.append(createBlockBtn());
            buttonBox.append(createDeleteBtn());
            label.textContent = 'Активен';
            break;
        case 'rejected':
            buttonBox.append(createDeleteBtn());
            label.textContent = 'Неподтвержден';
            break;
        case 'blocked':
            buttonBox.append(createUnblockBtn());
            label.textContent = 'Доступ ограничен';
            break;
        case 'total_blocked':
            buttonBox.append(createUnblockBtn());
            label.textContent = 'Полная блокировка';
            break;
        case 'deleted':
            buttonBox.append(createRecoveryBtn());
            label.textContent = 'Удален';
            break;
        case 'admin':
            label.textContent = 'Администратор';
            buttonBox.append(createEditAdminBtn());
            buttonBox.append(createDeleteAdminBtn());
            break;
        case 'moderator':
            label.textContent = 'Модератор';
            buttonBox.append(createEditAdminBtn());
            buttonBox.append(createDeleteAdminBtn());
            break;
        default:
            label.textContent = '';
    }
}

// CARD BUTTONS
function createBtn(className, title, adminOnly) {
    let btn = document.createElement('button');
    btn.classList.add('card__btn');
    btn.classList.add('card__btn--' + className);
    btn.setAttribute('title', title);
    if (adminOnly) btn.classList.add('adminOnly');
    return btn;
}

function createBlockBtn() {
    let btn = createBtn('unblock', 'Заблокировать', false);
    btn.addEventListener('click', function (e) {
        selectBlockStatus(e);
    });
    return btn;
}

function createUnblockBtn() {
    let btn = createBtn('block', 'Разблокировать', false);
    btn.addEventListener('click', function (e) {
        unblockPerson(e);
    });
    return btn;
}

function createRecoveryBtn() {
    let btn = createBtn('recovery', 'Восстановить', true);
    btn.addEventListener('click', function (e) {
        recoveryPerson(e);
    });
    return btn;
}

function createDeleteBtn() {
    let btn = createBtn('delete', 'Удалиь', true);
    btn.addEventListener('click', function (e) {
        deletePerson(e);
    });
    return btn;
}

function createCloseBtn() {
    return createBtn('close', 'Закрыть', false);
}

function createEditAdminBtn() {
    let btn = createBtn('admin-edit', 'Изменить статус', false);
    btn.addEventListener('click', function (e) {
        addEditAdminPanel(e);
    });
    return btn;
}

function createDeleteAdminBtn() {
    let btn = createBtn('admin-delete', 'Удалить', false);
    btn.addEventListener('click', function (e) {
        deleteAdmin(e);
    });
    return btn;
}

function createAddApplicantBtn() {
    let btn = createBtn('add-applicant', 'Добавить', false);
    btn.addEventListener('click', function (e) {
        addApplicantPanel(e);
    })
    return btn;
}

function createDeleteApplicantBtn() {
    let btn = createBtn('delete-applicant', 'Удалить', false);
    btn.addEventListener('click', function (e) {
        deleteApplicantCard(e);
    });
    return btn;
}

// 3 BUTTON POPUP
function createBtnInfo(name, link, callback, error) {
    return {
        name: name,
        link: link,
        callback: callback,
        error: error
    }
}

function createSelectorPanel(info1, info2, color) {
    let prev = document.querySelector('.panel');
    if (prev !== null) prev.remove();
    let panel = document.createElement('div'),
        btn1 = document.createElement('button'),
        btn2 = document.createElement('button'),
        cancel = document.createElement('button');

    panel.classList.add('panel');
    btn1.classList.add('panel__btn');
    btn1.classList.add('panel__btn--' + color);
    btn2.classList.add('panel__btn');
    btn2.classList.add('panel__btn--' + color);
    cancel.classList.add('panel__btn');
    cancel.classList.add('panel__btn--cancel');

    btn1.textContent = info1.name;
    btn2.textContent = info2.name;
    cancel.textContent = 'Отмена';

    panel.append(btn1);
    panel.append(btn2);
    panel.append(cancel);

    btn1.addEventListener('click', function (e) {
        console.log('Нажата кнопка 1');
        sendPostRequest('', info1.link, info1.callback, info1.error);
        e.target.parentElement.remove();
    });
    btn2.addEventListener('click', function (e) {
        console.log('Нажата кнопка 2');
        sendPostRequest('', info2.link, info2.callback, info2.error);
        e.target.parentElement.remove();
    });
    cancel.addEventListener('click', function (e) {
        console.log('отмена');
        e.target.parentElement.remove();
    });

    return panel;
}

// PERSONS CARD
function getPersonStatus(approval, block) {
    if (approval !== 'APPROVED') {
        return approval.toLowerCase();
    } else if (block !== 'UNBLOCKED') {
        return block.toLowerCase();
    } else {
        return 'active';
    }
}

function getAgeString(age) {
    if (age % 10 === 1) {
        return age + " год";
    } else if (age % 10 > 4 || age % 10 === 0 || (age > 10 && age < 15)) {
        return age + " лет";
    } else {
        return age + " года"
    }
}

function createPersonCard(response) {
    let item = document.createElement('div'),
        status = getPersonStatus(response.approval, response.block),
        infoBox = document.createElement('div'),
        buttonBox = document.createElement('div'),
        label = document.createElement('div'),
        img = document.createElement('img'),
        textBox = document.createElement('div'),
        name = document.createElement('p'),
        description = document.createElement('p');

    item.classList.add('card');
    item.classList.add(status)
    item.setAttribute('id', response.id);
    item.setAttribute('gx-attribute', status);
    infoBox.classList.add('card__info-box');
    buttonBox.classList.add('card__button-box');
    label.classList.add('card__label');
    img.classList.add('card__img');
    img.setAttribute('src', response.imageUrl);
    img.setAttribute('alt', 'Аватар пользователя');
    textBox.classList.add('card__text-box');
    name.classList.add('card__name');
    name.textContent = response.name;
    description.classList.add('card__description');
    let age = response.age !== null ? getAgeString(response.age) : 'нет данных',
        city = response.city !== null ? response.city : 'нет данных';
    description.textContent = age + ', ' + city;

    textBox.append(name);
    textBox.append(description);
    infoBox.append(img);
    infoBox.append(textBox);
    item.append(infoBox);
    item.append(buttonBox);
    item.append(label);

    markCard(item, status);

    return item;
}

function setPersonCard(response) {
    console.log('Выполняется setPersonCard');
    personsPageContent.textContent = '';
    for (let i = 0; i < response.length; i++) {
        let item = createPersonCard(response[i]);
        personsPageContent.append(item);
    }
    personsItems = personsPage.querySelectorAll('.card');
    personsCounter.textContent = response.length;
}

// PERSONS CARD FUNCTION
function getClickId(element) {
    return element.getAttribute('id');
}

function getGreatParent(element) {
    return element.parentElement.parentElement;
}

function markCardAsActive(element) {
    markCard(element, 'active');
}

function markCardAsDeleted(element) {
    markCard(element, 'deleted');
}

function createPersonSelectorPanel(e) {
    let element = getGreatParent(e.target),
        id = getClickId(element);
    const markAsBlocked = function () {
            markCard(element, 'blocked')
        },
        markAsTotalBlocked = function () {
            markCard(element, 'total_blocked')
        };
    let info1 = createBtnInfo('Ограничить доступ', '/admin/block/' + id, markAsBlocked, 'Блокировка не удалась'),
        info2 = createBtnInfo('Заблокировать', '/admin/total/' + id, markAsTotalBlocked, 'Блокировка не удалась');
    return createSelectorPanel(info1, info2, 'red');
}

function selectBlockStatus(e) {
    getGreatParent(e.target).append(createPersonSelectorPanel(e));
}

function unblockPerson(e) {
    let element = getGreatParent(e.target);
    const mark = function () {
        markCardAsActive(element);
    };
    sendPostRequest('', '/admin/unblock/' + getClickId(element), mark, 'Разблокировка не удалась')
}

function deletePerson(e) {
    let element = getGreatParent(e.target);
    const mark = function () {
        markCardAsDeleted(element);
    };
    sendPostRequest('', '/admin/delete/' + getClickId(element), mark, 'Удаление не удалось')
}

function recoveryPerson(e) {
    let element = getGreatParent(e.target);
    const mark = function () {
        markCardAsActive(element);
    };
    sendPostRequest('', '/admin/restore/' + getClickId(element), mark, 'Восстановние не удалось')
}

// ADMIN CARD
function createAdminCard(response) {
    let buttonBox = createButtonBox(),
        status = response.status === 'ADMIN' ? 'admin' : 'moderator';
    let item = createCard(response.id, response.imageUrl, response.name, '', buttonBox, 'Администраторы');
    item.setAttribute('gx-attribute', status, '');
    markCard(item, status);
    return item;
}

function setAdminCard(response) {
    console.log('Выполняется setAdminCard');
    adminsPageContent.textContent = '';
    for (let i = 0; i < response.length; i++) {
        let item = createAdminCard(response[i]);
        adminsPageContent.append(item);
    }
    adminsItems = adminsPage.querySelectorAll('.card');
    adminsCounter.textContent = response.length;
}

function createEditAdminSelectorPanel(e) {
    let element = getGreatParent(e.target),
        id = getClickId(element);
    const markAsAdmin = function () {
            markCard(element, 'admin');
        },
        markAsModerator = function () {
            markCard(element, 'moderator');
        };
    let info1 = createBtnInfo('Администратор', '/admin/' + id + '/admin', markAsAdmin, 'Назначение администратора не удалось'),
        info2 = createBtnInfo('Модератор', '/admin/' + id + '/moderator', markAsModerator, 'Назначение модератора не удалось');
    return createSelectorPanel(info1, info2, 'green');
}

function addEditAdminPanel(e) {
    getGreatParent(e.target).append(createEditAdminSelectorPanel(e));
}

function deleteAdmin(e) {
    let element = getGreatParent(e.target),
        id = getClickId(element);
    const remove = function () {
        element.remove();
    };
    sendDeleteRequest('', '/admin/' + id, remove, 'Не удалось удалить админа');
}

// APPLICANTS
function createApplicantCard(response) {
    let description = getAgeString(response.age) + ', ' + response.country + ', ' + response.city,
        buttonBox = createButtonBox(),
        label = 'Посты: ' + response.posts + ', ' + 'Комментарии: ' + response.comments;
    buttonBox.append(createAddApplicantBtn());
    buttonBox.append(createDeleteApplicantBtn());
    return createCard(response.id, response.imageUrl, response.name, description, buttonBox, label, '');
}

function displayApplicants(response) {
    addPageTitle.textContent = 'Кандидаты';
    addPageContent.classList.add('displayFlexAndWrap');
    response.forEach(el => addPageContent.append(createApplicantCard(el)));
}

let applicantsLink = adminsPage.querySelector('.admin-page__add-link');
applicantsLink.addEventListener('click', function (e) {
    e.preventDefault();
    adminsPage.style.display = 'none';
    addPage.style.display = 'block';
    sendGetRequest('/admin/applicants', displayApplicants, 'Список кандидатов не загружен');

})

function createApplicantSelectorPanel(e) {
    let element = getGreatParent(e.target),
        id = getClickId(element);
    const markAsAdmin = function () {
            markCard(element, 'admin');
        },
        markAsModerator = function () {
            markCard(element, 'moderator');
        };
    let info1 = createBtnInfo('Администратор', '/admin/add/admin/' + id, markAsAdmin, 'Назначение администратора не удалось'),
        info2 = createBtnInfo('Модератор', '/admin/add/moderator/' + id, markAsModerator, 'Назначение модератора не удалось');
    return createSelectorPanel(info1, info2, 'green');
}

function addApplicantPanel(e) {
    getGreatParent(e.target).append(createApplicantSelectorPanel(e));
}

function deleteApplicantCard(e) {
    getGreatParent(e.target).remove();
}


//ORDER CARD
function createOrderCard(response) {
    let item = document.createElement('div'),
        infoBox = document.createElement('div'),
        label = document.createElement('div'),
        number = document.createElement('div'),
        img = document.createElement('img'),
        textBox = document.createElement('div'),
        name = document.createElement('p'),
        description = document.createElement('p');

    item.classList.add('card');
    item.setAttribute('id', response.id);
    item.classList.add('order__card');
    item.classList.add(response.status.toLowerCase());
    item.setAttribute('gx-attribute', response.status.toLowerCase());
    infoBox.classList.add('card__info-box');
    label.classList.add('card__label');
    label.textContent = response.status === 'NEW' ? 'Новое' : response.status === 'OPEN' ? 'В работе' : 'Закрытое';
    number.classList.add('order__number');
    number.textContent = '#' + response.id;
    img.classList.add('card__img');
    img.setAttribute('src', response.imageUrl);
    img.setAttribute('alt', 'Аватар пользователя');
    name.classList.add('card__name');
    name.textContent = response.name;
    description.classList.add('card__description');
    description.textContent = response.title;

    textBox.append(name);
    textBox.append(description);
    infoBox.append(img);
    infoBox.append(textBox);
    item.append(infoBox);
    item.append(label);
    item.append(number);

    return item;
}

function createOrderMessage(response) {
    let name = check(response.admin) ? response.admin : response.customer;
    let message = createCard('', '', name, response.text, '', response.date, '');
    if (check(response.customer)) {
        message.classList.add('customerMessage');
    } else {
        message.classList.add('adminMessage');
    }
    return message;
}

function setCloseOrder() {
    addPage.querySelector('.order__close').remove();
    addPageContent.querySelector('form').remove();
    let message = createOrderMessage({text: 'Обращение закрыто'});
    addPageContent.append(message);
}

function setAnswer() {
    let textarea = addPageContent.querySelector('textarea'),
        text = textarea.value,
        message = createOrderMessage({text: text});
    addPageContent.querySelector('form').before(message);
    textarea.value = '';
}

function displayOrder(response) {
    addPageTitle.textContent = 'ОБРАЩЕНИЕ #' + response.id;
    if (response.status !== 'CLOSED') {
        let close = document.createElement('button');
        close.classList.add('order__close');
        close.textContent = 'закрыть обращение';
        close.setAttribute('order', response.id);
        addPageTitle.append(close);
        close.addEventListener('click', function () {
            let id = this.getAttribute('order');
            sendPostRequest('', '/support/close/' + id, setCloseOrder, 'Обращение ' + id + ' не закрыто');
        })
    }
    let messages = response.messageList;
    messages.forEach(el => addPageContent.append(createOrderMessage(el)));
    if (response.status !== 'CLOSED') {
        let contactForm = document.createElement('form'),
            textArea = document.createElement('textarea'),
            submit = document.createElement('button');
        textArea.classList.add('order__textarea');
        textArea.placeholder = 'Текст сообщения...';
        submit.classList.add('order__submit');
        submit.textContent = 'ответить';
        submit.setAttribute('order', response.id);
        submit.addEventListener('click', function (e) {
            e.preventDefault();
            let id = this.getAttribute('order'),
                text = addPageContent.querySelector('.order__textarea').value;
            sendPostRequest(text, '/support/answer/' + id, setAnswer, 'Ответ не записан');
        })
        contactForm.append(textArea);
        contactForm.append(submit);
        addPageContent.append(contactForm);
    }
}

function setOrderCard(response) {
    console.log('Выполняется setOrderCard');
    supportsPageContent.textContent = '';
    for (let i = 0; i < response.length; i++) {
        let item = createOrderCard(response[i]);
        supportsPageContent.append(item);
    }
    supportsItems = supportsPage.querySelectorAll('.card');
    supportsCounter.textContent = response.length;

    supportsItems.forEach(el => el.addEventListener('click', function () {
        let id = this.getAttribute('id');
        supportsPage.style.display = 'none';
        addPage.style.display = 'block';
        addPageContent.classList.remove('displayFlexAndWrap');
        sendGetRequest('/support/order/' + id, displayOrder, 'Обращние id:' + id + ' не загружено')
    }));
}


window.addEventListener('DOMContentLoaded', function () {

    let adminToken = localStorage.getItem('adminToken');
    if (adminToken !== null) {
        getUserByToken();
    }

    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
        getUser();
    });

    // LOGOUT
    let exit = document.querySelector('.exit');
    exit.addEventListener('click', function (e) {
        e.preventDefault();
        loginPage.style.display = 'block';
        adminPanel.style.display = 'none';
        let inputs = adminPanel.querySelectorAll('input');
        inputs.forEach(el => el.value = '');
        localStorage.removeItem('adminToken');
        localStorage.removeItem('adminPage');
    });

})
