// ==========================================
// 파일 관리 관련 JavaScript
// ==========================================

/**
 * 파일 업로드
 */
async function uploadFiles() {
    const fileInput = document.getElementById('fileInput');
    const files = fileInput.files;
    const statusDiv = document.getElementById('uploadStatus');

    if (!statusDiv) {
        console.error('uploadStatus 엘리먼트를 찾을 수 없습니다');
        return;
    }

    // assignmentId 확인
    if (typeof assignmentId === 'undefined') {
        showAlert(statusDiv, '과제 ID를 찾을 수 없습니다', 'danger');
        return;
    }

    if (files.length === 0) {
        showAlert(statusDiv, '파일을 선택하세요', 'danger');
        return;
    }

    showAlert(statusDiv, '파일 업로드 중...', 'info');

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const formData = new FormData();
        formData.append('file', file);
        formData.append('assignmentId', assignmentId);

        try {
            const response = await fetch('/api/file/upload', {
                method: 'POST',
                body: formData
            });

            const data = await response.json();

            if (data.success) {
                showAlert(statusDiv,
                    `✅ 파일 업로드 완료: ${data.originalFilename} (${formatFileSize(data.fileSize)})`,
                    'success');
                fileInput.value = '';

                // 페이지 새로고침하여 제출 목록 업데이트
                setTimeout(() => {
                    location.reload();
                }, 1500);
            } else {
                showAlert(statusDiv,
                    `❌ 업로드 실패: ${data.message}`,
                    'danger');
            }
        } catch (error) {
            console.error('파일 업로드 오류:', error);
            showAlert(statusDiv,
                `오류 발생: ${error.message}`,
                'danger');
        }
    }
}

/**
 * 파일 다운로드
 */
function downloadFile(filename) {
    if (!filename || filename.trim() === '') {
        alert('유효한 파일명이 필요합니다');
        return;
    }

    try {
        // 간단한 방법: 직접 링크 클릭
        const link = document.createElement('a');
        link.href = `/api/file/download/${encodeURIComponent(filename)}`;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        console.log(`파일 다운로드 시작: ${filename}`);
    } catch (error) {
        console.error('파일 다운로드 오류:', error);
        alert('파일 다운로드에 실패했습니다');
    }
}

/**
 * 파일 삭제
 */
async function deleteFile(filename) {
    if (!filename || filename.trim() === '') {
        alert('유효한 파일명이 필요합니다');
        return;
    }

    if (!confirm(`파일을 삭제하시겠습니까?\n${filename}`)) {
        return;
    }

    try {
        const response = await fetch(`/api/file/delete/${encodeURIComponent(filename)}`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (data.success) {
            alert('✅ 파일이 삭제되었습니다');
            location.reload();
        } else {
            alert(`❌ 삭제 실패: ${data.message}`);
        }
    } catch (error) {
        console.error('파일 삭제 오류:', error);
        alert('파일 삭제에 실패했습니다');
    }
}

/**
 * 파일 존재 여부 확인
 */
async function checkFileExists(filename) {
    try {
        const response = await fetch(`/api/file/exists/${encodeURIComponent(filename)}`);
        const data = await response.json();

        if (data.success) {
            return data.exists;
        }
        return false;
    } catch (error) {
        console.error('파일 확인 오류:', error);
        return false;
    }
}

/**
 * 알림 메시지 표시
 */
function showAlert(element, message, type = 'info') {
    const alertClass = `alert alert-${type}`;
    element.innerHTML = `<div class="${alertClass}">${message}</div>`;

    // 5초 후 자동 사라짐
    setTimeout(() => {
        element.innerHTML = '';
    }, 5000);
}

/**
 * 파일 크기 포맷팅 (Bytes to Human Readable)
 */
function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

/**
 * 업로드된 파일 목록 로드 (필요시 구현)
 */
function loadUploadedFiles() {
    // API 호출하여 파일 목록 로드
    // 필요에 따라 구현
    console.log('파일 목록 로드');
}

// ==========================================
// 페이지 로드 시 초기화
// ==========================================
document.addEventListener('DOMContentLoaded', function() {
    console.log('파일 관리 스크립트 로드 완료');

    // 드래그 앤 드롭 지원 (선택사항)
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
        const dropZone = document.getElementById('fileInput').parentElement;

        if (dropZone) {
            dropZone.addEventListener('dragover', function(e) {
                e.preventDefault();
                dropZone.style.backgroundColor = '#f0f0f0';
            });

            dropZone.addEventListener('dragleave', function(e) {
                e.preventDefault();
                dropZone.style.backgroundColor = '';
            });

            dropZone.addEventListener('drop', function(e) {
                e.preventDefault();
                dropZone.style.backgroundColor = '';

                const files = e.dataTransfer.files;
                fileInput.files = files;
            });
        }
    }
});