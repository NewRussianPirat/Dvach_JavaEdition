document.addEventListener('DOMContentLoaded', function () {
    let buttonId = 'createTopicButton';
    let formId = 'createTopicForm';
    let isTopicForm = false;

    if (!document.getElementById(buttonId)) {
        buttonId = 'answerButton';
        formId = 'answerForm';
        isTopicForm = true;
    }

    let button = document.getElementById(buttonId);
    let form = document.getElementById(formId);

    button.addEventListener('click', function () {
        if (form.style.display === 'none') {
            form.style.display = 'block';
            button.textContent = 'Закрыть форму';
        } else {
            form.style.display = 'none';
            button.textContent = isTopicForm ? 'Ответить' : 'Создать тему';
        }
    });

    let uploadButton = document.getElementById('upload-button');
    let fileInput = document.getElementById('file-input');
    let preview = document.getElementById('preview');
    let files = [];
    let buttonAddedId = 0;

    uploadButton.addEventListener('click', function () {
        if (files.length >= 4) {
            alert("К сообщению можно прикрепить только 4 файла");
        } else {
            fileInput.click();
        }
    });

    fileInput.addEventListener('change', function () {
        for (let i = 0; i < fileInput.files.length; ++i) {
            if (files.length >= 4) {
                break;
            }

            let uploadedFile = fileInput.files[i]
            files.push(uploadedFile);

            let img = document.createElement('img');
            img.src = URL.createObjectURL(uploadedFile);
            img.height = 60; // уменьшенная версия
            img.onload = function () {
                URL.revokeObjectURL(img.src);
            }

            let removeButton = document.createElement('button');
            removeButton.className = 'removeButton';
            removeButton.id = 'removeButton' + buttonAddedId;
            removeButton.textContent = '×';
            removeButton.widht = img.width;
            removeButton.addEventListener('click', function () {
                let index = files.indexOf(uploadedFile);
                if (index > -1) {
                    files.splice(index, 1);
                }
                preview.removeChild(fileContainer);
            });

            let fileContainer = document.createElement('div');
            fileContainer.className = 'addedImage';
            fileContainer.id = 'addedImage' + buttonAddedId;
            fileContainer.appendChild(img);
            fileContainer.appendChild(removeButton);

            preview.appendChild(fileContainer);
            ++buttonAddedId;
        }
        fileInput.value = '';
    });

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Предотвратить отправку формы браузером
        //
        let formData = new FormData();
        if (!isTopicForm) {
            formData.append('topicName', document.getElementById('topicName').value);
        }
        formData.append('postContent', document.getElementById('postContent').value);
        for (let file of files) {
            formData.append('file-input', file);
        }
        //
        let article = document.getElementById('article').value;
        let topicId = null;
        if (isTopicForm) {
            topicId = document.getElementById('topicId').value;
        }
        buttonAddedId = 0;
        fetch('/section/' + article + (isTopicForm ? ('/topic/' + topicId + '/answer') : '/createTopic'), {
            method: 'POST',
            body: formData
        }).then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        }).then(url => {
            location.href = url;
        }).catch(error => {
            console.error('Отсутствуют тема или текст поста', error);
            alert(error);
        });
    });
});
