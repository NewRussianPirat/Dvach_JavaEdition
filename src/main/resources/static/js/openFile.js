let scale = 1;
let scaleStep = 0.1;

let draggable = document.querySelector('.draggable');
let isDragging = false;
let startX = 0;
let startY = 0;
let offsetX = 0;
let offsetY = 0;

draggable.addEventListener('mousedown', function (event) {
    event.preventDefault();
    startX = event.clientX - offsetX;
    startY = event.clientY - offsetY;
    isDragging = true;
    draggable.style.transformOrigin = `${event.clientX}px ${event.clientY}px`;
});

document.addEventListener('mousemove', function (event) {
    if (isDragging) {
        offsetX = event.clientX - startX;
        offsetY = event.clientY - startY;
        draggable.style.transform = `scale(${scale}) translate(${offsetX}px, ${offsetY}px)`;
    }
});

draggable.addEventListener('mouseup', function () {
    isDragging = false;
});

document.addEventListener('mouseleave', function () {
    isDragging = false;
});

window.addEventListener('blur', function () {
    isDragging = false;
});

function openModal(modalId, fileId, src) {
    let modal = document.getElementById(modalId);
    let file = document.getElementById(fileId);

    modal.style.display = 'flex';
    modal.addEventListener('click', function (event) {
        closeModal(modalId, fileId, event);
    });

    if (fileId === 'vid01') {
        document.addEventListener('keydown', function (event) {
            if (event.code === 'Space') {
                event.preventDefault();
                if (file.paused) {
                    file.play();
                } else {
                    file.pause();
                }
            }
        })
    }

    file.src = src;
    file.addEventListener('wheel', zoom);

}

function closeModal(modalId, fileId, event) {
    if (event.target.id !== fileId) {
        document.getElementById(modalId).style.display = 'none';
        document.getElementById(fileId).removeEventListener('wheel', zoom);
        document.getElementById(fileId).style.transform = 'scale(1) translate(0, 0)';
        if (fileId === 'vid01') {
            document.getElementById(fileId).src = '';
        }
        scale = 1;
        startX = 0;
        startY = 0;
        offsetX = 0;
        offsetY = 0;
    }
}

function zoom(event) {
    event.preventDefault();

    if (event.deltaY < 0) {
        scale += scaleStep;
    } else if (scale > 0.5) {
        scale -= scaleStep;
    }

    let rect = event.target.getBoundingClientRect();
    let x = event.clientX - rect.left;
    let y = event.clientY - rect.top;

    event.target.style.transformOrigin = `${x}px ${y}px`;

    event.target.style.transform = `scale(${scale}) translate(${offsetX}px, ${offsetY}px)`;
}