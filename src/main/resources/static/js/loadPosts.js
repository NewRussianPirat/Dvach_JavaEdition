function createButton(isFirstPost, posts, article, topicId) {
    let button = document.createElement('button');
    button.className = 'loadPosts'
    if (posts.length === 4) {
        button.id = 'allPosts';
        button.textContent = 'Показать все посты';
    } else {
        button.id = 'notAllPosts';
        button.textContent = 'Свернуть все посты';
    }

    button.addEventListener('click', function () {
        let currentTopic = this.parentNode.parentNode;
        let topicTitleText = currentTopic.dataset.topicTitle;
        let topicId = currentTopic.dataset.topicId;
        let article = document.getElementById('article').value;
        let isAdmin = document.getElementById('isAdmin').value;

        let method = button.id === 'allPosts' ? '/showAllPosts' : '/showNotAllPosts';

        let postsArray = [];

        fetch('/section/' + article + '/topic/' + topicId + method)
            .then(response => response.json())
            .then(posts => {
                let isFirstPost = true;
                posts.forEach(postWithFilesList => {
                    let post = postWithFilesList.post;
                    let files = postWithFilesList.files;

                    let postElement = document.createElement('div');
                    postElement.classList.add('post');
                    if (isFirstPost) {
                        postElement.classList.add('first-post');
                    }

                    let postHeader = document.createElement('div');
                    postHeader.className = 'post-header';

                    if (isFirstPost) {
                        let topicTitle = document.createElement('span');
                        topicTitle.id = 'topic-Title'
                        topicTitle.textContent = topicTitleText;
                        postHeader.appendChild(topicTitle);
                    }

                    let postDateTime = document.createElement('span');
                    postDateTime.id = 'post-datetime';
                    postDateTime.textContent = post.datetime;
                    postHeader.appendChild(postDateTime);

                    if (isFirstPost) {
                        let topicReference = document.createElement('span');
                        topicReference.id = 'topic-reference';

                        let ref = document.createElement('a');
                        ref.href = '/section/' + article + '/topic/' + topicId;
                        ref.textContent = 'Ответить';

                        topicReference.appendChild(ref);
                        postHeader.appendChild(topicReference);
                    }

                    if (isAdmin === 'true') {
                        let deleteBlock = document.createElement('div');
                        deleteBlock.id = 'deleteBlock';

                        let deleteForm = document.createElement('form');
                        deleteForm.id = 'deleteForm';
                        deleteForm.method = 'post';
                        if (isFirstPost) {
                            deleteForm.action = '/section/' + article + '/deleteTopic';
                        } else {
                            deleteForm.action = '/section/' + article + '/deletePost';
                        }

                        let deleteInput = document.createElement('input');
                        deleteInput.id = 'deleteInput';
                        deleteInput.type = 'hidden';
                        if (isFirstPost) {
                            deleteInput.name = 'topic';
                            deleteInput.value = topicId;
                        } else {
                            deleteInput.name = 'post';
                            deleteInput.value = post.id;
                        }
                        deleteInput.value;

                        let deleteButton = document.createElement('button');
                        deleteButton.id = 'deleteButton';
                        deleteButton.type = 'submit';
                        if (isFirstPost) {
                            deleteButton.textContent = 'Удалить обсуждение';
                        } else {
                            deleteButton.textContent = 'Удалить пост';
                        }

                        deleteForm.appendChild(deleteInput);
                        deleteForm.appendChild(deleteButton);
                        deleteBlock.appendChild(deleteForm);
                        postHeader.appendChild(deleteBlock);
                    }

                    let isAdminInput = document.createElement('input');
                    isAdminInput.id = 'isAdmin';
                    isAdminInput.type = 'hidden';
                    isAdminInput.value = isAdmin;
                    postHeader.appendChild(isAdminInput);

                    let postBlock = document.createElement('div');
                    postBlock.classList.add('post-block');
                    if (files != null) {
                        if (files.length === 1) {
                            postBlock.classList.add('single-file-post');
                        }
                    }

                    let postFiles = document.createElement('div');
                    postFiles.className = 'post-files';

                    if (files != null) {
                        files.forEach(file => {
                            let fileInfo = document.createElement('div');
                            fileInfo.className = "file-info";

                            let fileName = document.createElement('span');
                            fileName.className = 'file-name';
                            fileName.textContent = file.name;

                            let fileSizeDimensions = document.createElement('span');
                            fileSizeDimensions.className = 'file-size-dimensions';
                            fileSizeDimensions.textContent = file.size + 'Кб, ';

                            fileInfo.appendChild(fileName);
                            fileInfo.appendChild(fileSizeDimensions);

                            if (file.type === 'image') {
                                let image = document.createElement('img');
                                image.src = '/section/' + article + '/' + topicId + '/' + post.id + '/' + file.name;
                                image.alt = 'Post image';
                                image.onclick = function () {
                                    openModal('imageModal', 'img01', image.src);
                                };

                                let imageDiv = document.createElement('div');
                                imageDiv.appendChild(fileInfo);
                                imageDiv.appendChild(image);

                                postFiles.appendChild(imageDiv);
                            } else if (file.type === 'video') {
                                let video = document.createElement('video');
                                video.src = '/section/' + article + '/' + topicId + '/' + post.id + '/' + file.name;
                                video.alt = 'Post video';
                                video.onclick = function () {
                                    openModal('videoModal', 'vid01', video.src);
                                };

                                let videoDiv = document.createElement('div');
                                videoDiv.appendChild(fileInfo);
                                videoDiv.appendChild(video);

                                postFiles.appendChild(videoDiv);
                            }
                        });
                    }

                    let content = document.createElement('p');
                    content.textContent = post.content;

                    postBlock.appendChild(postFiles);
                    postBlock.appendChild(content);

                    postElement.appendChild(postHeader);
                    postElement.appendChild(postBlock);

                    if (isFirstPost) {
                        let button = createButton(isFirstPost, posts, article, topicId);
                        postElement.appendChild(button);
                        isFirstPost = false;
                    }

                    postsArray.push(postElement);
                });
            })
            .then(() => {
                while (currentTopic.firstChild) {
                    currentTopic.removeChild(currentTopic.firstChild);
                }

                postsArray.forEach(post => {
                    currentTopic.appendChild(post);
                });
            })
    });

    return button;
}

document.querySelectorAll('.loadPosts').forEach(function (button) {
    button.addEventListener('click', function () {
        let currentTopic = this.parentNode.parentNode;
        let topicTitleText = currentTopic.dataset.topicTitle;
        let topicId = currentTopic.dataset.topicId;
        let article = document.getElementById('article').value;
        let isAdmin = document.getElementById('isAdmin').value;

        let method = button.id === 'allPosts' ? '/showAllPosts' : '/showNotAllPosts';

        while (currentTopic.firstChild) {
            currentTopic.removeChild(currentTopic.firstChild);
        }

        fetch('/section/' + article + '/topic/' + topicId + method)
            .then(response => response.json())
            .then(posts => {
                let isFirstPost = true;
                posts.forEach(postWithFilesList => {
                    let post = postWithFilesList.post;
                    let files = postWithFilesList.files;

                    let postElement = document.createElement('div');
                    postElement.classList.add('post');
                    if (isFirstPost) {
                        postElement.classList.add('first-post');
                    }

                    let postHeader = document.createElement('div');
                    postHeader.className = 'post-header';

                    if (isFirstPost) {
                        let topicTitle = document.createElement('span');
                        topicTitle.id = 'topic-Title'
                        topicTitle.textContent = topicTitleText;
                        postHeader.appendChild(topicTitle);
                    }

                    let postDateTime = document.createElement('span');
                    postDateTime.id = 'post-datetime';
                    postDateTime.textContent = post.datetime;
                    postHeader.appendChild(postDateTime);

                    if (isFirstPost) {
                        let topicReference = document.createElement('span');
                        topicReference.id = "topic-reference";

                        let ref = document.createElement('a');
                        ref.href = '/section/' + article + '/topic/' + topicId;
                        ref.textContent = 'Ответить';

                        topicReference.appendChild(ref);
                        postHeader.appendChild(topicReference);
                    }

                    if (isAdmin === 'true') {
                        let deleteBlock = document.createElement('div');
                        deleteBlock.id = 'deleteBlock';

                        let deleteForm = document.createElement('form');
                        deleteForm.id = 'deleteForm';
                        deleteForm.method = 'post';
                        if (isFirstPost) {
                            deleteForm.action = '/section/' + article + '/deleteTopic';
                        } else {
                            deleteForm.action = '/section/' + article + '/deletePost';
                        }

                        let deleteInput = document.createElement('input');
                        deleteInput.id = 'deleteInput';
                        deleteInput.type = 'hidden';
                        if (isFirstPost) {
                            deleteInput.name = 'topic';
                            deleteInput.value = topicId;
                        } else {
                            deleteInput.name = 'post';
                            deleteInput.value = post.id;
                        }
                        deleteInput.value;

                        let deleteButton = document.createElement('button');
                        deleteButton.id = 'deleteButton';
                        deleteButton.type = 'submit';
                        if (isFirstPost) {
                            deleteButton.textContent = 'Удалить обсуждение';
                        } else {
                            deleteButton.textContent = 'Удалить пост';
                        }

                        deleteForm.appendChild(deleteInput);
                        deleteForm.appendChild(deleteButton);
                        deleteBlock.appendChild(deleteForm);
                        postHeader.appendChild(deleteBlock);
                    }

                    let isAdminInput = document.createElement('input');
                    isAdminInput.id = 'isAdmin';
                    isAdminInput.type = 'hidden';
                    isAdminInput.value = isAdmin;
                    postHeader.appendChild(isAdminInput);

                    let postBlock = document.createElement('div');
                    postBlock.classList.add('post-block');
                    if (files != null) {
                        if (files.length === 1) {
                            postBlock.classList.add('single-file-post');
                        }
                    }

                    let postFiles = document.createElement('div');
                    postFiles.className = 'post-files';

                    if (files != null) {
                        files.forEach(file => {
                            let fileInfo = document.createElement('div');
                            fileInfo.className = "file-info";

                            let fileName = document.createElement('span');
                            fileName.className = 'file-name';
                            fileName.textContent = file.name;

                            let fileSizeDimensions = document.createElement('span');
                            fileSizeDimensions.className = 'file-size-dimensions';
                            fileSizeDimensions.textContent = file.size + 'Кб, ';

                            fileInfo.appendChild(fileName);
                            fileInfo.appendChild(fileSizeDimensions);

                            if (file.type === 'image') {
                                let image = document.createElement('img');
                                image.src = '/section/' + article + '/' + topicId + '/' + post.id + '/' + file.name;
                                image.alt = 'Post image';
                                image.onclick = function () {
                                    openModal('imageModal', 'img01', image.src);
                                };

                                let imageDiv = document.createElement('div');
                                imageDiv.appendChild(fileInfo);
                                imageDiv.appendChild(image);

                                postFiles.appendChild(imageDiv);
                            } else if (file.type === 'video') {
                                let video = document.createElement('video');
                                video.src = '/section/' + article + '/' + topicId + '/' + post.id + '/' + file.name;
                                video.alt = 'Post video';
                                video.onclick = function () {
                                    openModal('videoModal', 'vid01', video.src);
                                };

                                let videoDiv = document.createElement('div');
                                videoDiv.appendChild(fileInfo);
                                videoDiv.appendChild(video);

                                postFiles.appendChild(videoDiv);
                            }
                        });
                    }

                    let content = document.createElement('p');
                    content.textContent = post.content;

                    postBlock.appendChild(postFiles);
                    postBlock.appendChild(content);

                    postElement.appendChild(postHeader);
                    postElement.appendChild(postBlock);

                    if (isFirstPost) {
                        let button = createButton(isFirstPost, posts, article, topicId);
                        postElement.appendChild(button);
                        isFirstPost = false;
                    }

                    currentTopic.appendChild(postElement);
                });
            });
    });
});