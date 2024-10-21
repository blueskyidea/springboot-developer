//삭제 기능
const deleteButton = document.getElementById('delete-btn');

if(deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
//        fetch(`/api/articles/${id}`, {  //백틱(`)을 꼭 사용해줘야함! 이걸 템플릿 리터럴이라고 부르는데 자바스크립트에서 문자열을 입력하는 방식임.
//            method: 'DELETE'
//        })
//        .then(() => {  //fetch()가 잘 완료되면 연이어 실행되는 메서드
//            alert('삭제가 완료되었습니다.');
//            location.replace('/articles');  //실행 시 사용자의 웹 브라우저 화면을 현재 주소를 기반해 옮겨줌
//        });
        function success() {
            alert("삭제가 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("삭제 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", `/api/articles/` + id, null, success, fail);
    });
}

//수정 기능
//id가 modify-btn인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if(modifyButton) {
    //클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

//        fetch(`/api/articles/${id}`, {
//            method: 'PUT',
//            headers: {
//                "Content-Type": "application/json",
//            },
//            body: JSON.stringify({
//                title: document.getElementById('title').value,
//                content: document.getElementById('content').value
//            })
//        })
//        .then(() => {
//            alert('수정이 완료되었습니다.');
//            location.replace(`/articles/${id}`)
//        });

        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value
        })

        function success() {
            alert("수정 완료되었습니다");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("수정 실패했습니다.");
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}

//등록 기능
//id가 create-btn인 엘리먼트 조회
const createButton = document.getElementById('create-btn');

if(createButton) {
//    //클릭 이벤트가 감지되면 생성 API 요청
//    createButton.addEventListener('click', (event) => {
//        fetch("/api/articles", {
//            method: 'POST',
//            headers: {
//                "Content-Type": "application/json",
//            },
//            body: JSON.stringify({
//                title: document.getElementById('title').value,
//                content: document.getElementById('content').value
//            })
//        })
//        .then(() => {
//            alert('등록이 완료되었습니다.');
//            location.replace("/articles");
//        });
//    });

    //등록 버튼을 클릭하면 /api/articles로 요청을 보냄
    createButton.addEventListener("click", event => {
        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value
        });
        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        };
        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        };

        httpRequest("POST", `/api/articles`, body, success, fail)
    });
}

// 로그아웃 기능
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰을 삭제
            localStorage.removeItem('access_token');

            // 쿠키에 저장된 리프레시 토큰을 삭제
            deleteCookie('refresh_token');
            location.replace('/login');
        }
        function fail() {
            alert('로그아웃 실패했습니다.');
        }

        httpRequest('DELETE','/api/refresh-token', null, success, fail);
    });
}

// 쿠키를 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

/* POST 요청을 보낼 때 엑세스 토큰도 함께 보냄.
만약 응답에 권한이 없다는 에러 코드가 발생하면 리프레시 토큰과 함께 새로운 액세스 토큰 요청.
전달받은 액세스 토큰으로 다시 API 요청함.*/

//쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split("=");

        if(key == dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

//HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            //로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then((response) => {
        if(response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if(response.status === 401 && refresh_token) {
            fetch("/api/token", {
                method: 'POST',
                headers: {
                    Authorization: "Bearer" + localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    refreshToken: getCookie("refresh_token"),
                }),
            })
                .then((res) => {
                    if(res.ok) {
                        return res.json();
                    }
                })
                .then((result) => {
                    //재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem("access_token", result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch((error) => fail());
        } else {
            return fail();
        }
    });
}

//댓글 생성 기능
const commentCreateButton = document.getElementById('comment-create-btn');

if(commentCreateButton) {
    commentCreateButton.addEventListener('click', event => {
        articleId = document.getElementById('article-id').value;

        body = JSON.stringify({
            articleId: articleId,
            content: document.getElementById('content').value
        });
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles/' + articleId);
        };
        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles/' + articleId);
        };

        httpRequest('POST', '/api/comments', body, success, fail)
    });
}

//댓글을 순회하며 각 버튼에 이벤트 리스너를 등록함(querySelectorAll을 사용).
document.querySelectorAll('.comment-modify-btn').forEach(button => {
    button.addEventListener('click', event => {
        // 현재 클릭된 댓글의 ID 및 내용을 가져오기 위해 부모 요소 탐색
        const commentCard = event.target.closest('.card');
        const commentId = commentCard.querySelector('.comment-id').value; // 클래스 선택자 사용
        const commentContent = commentCard.querySelector('.comment-content').value; // 클래스 선택자 사용

        // textarea에 포커스 주기
        document.getElementById("content").focus(); // textarea에 포커스

        // textarea에 수정 전 내용 입력
        document.getElementById('content').value = commentContent;

        // 댓글 추가 버튼 숨기고 댓글 수정 버튼 보이게 하기
        document.getElementById('comment-create-btn').style.display = 'none';
        document.getElementById('comment-modify').style.display = 'block';

        // 수정 버튼 클릭 시 실행될 API 요청 (필요한 경우 수정)
        const modifyButton = document.getElementById('comment-modify'); // 수정 버튼을 변수에 저장
        modifyButton.onclick = () => { // addEventListener 대신 onclick 사용
            let articleId = document.getElementById('article-id').value;
            let body = JSON.stringify({
                content: document.getElementById("content").value
            });

            function success() {
                alert("수정 완료되었습니다");

                // DOM에서 댓글 내용 업데이트
                const updatedContent = document.getElementById("content").value;
                commentCard.querySelector('.comment-content').value = updatedContent; // hidden input update
                commentCard.querySelector('.card-text').textContent = updatedContent; // visible content update

                // 필요에 따라 해당 댓글로 스크롤
                commentCard.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }

            function fail() {
                alert("수정 실패했습니다.");
                location.replace("/articles/" + articleId);
            }

            // 댓글 수정 API 요청
            httpRequest("PUT", "/api/comments/" + commentId, body, success, fail);
        };
    });
});

document.querySelectorAll('.comment-delete-btn').forEach(button => {
    button.addEventListener('click', event => {
        // 현재 클릭된 댓글의 ID를 가져오기 위해 부모 요소 탐색
        const commentCard = event.target.closest('.card');
        const commentId = commentCard.querySelector('.comment-id').value; // 클래스 선택자 사용

        let articleId = document.getElementById('article-id').value;

        // 삭제 확인 후 API 요청
        if (confirm("댓글을 삭제하시겠습니까?")) {
            function success() {
                alert("삭제 완료되었습니다.");
                location.replace("/articles/" + articleId);
            }

            function fail() {
                alert("삭제 실패했습니다.");
                location.replace("/articles/" + articleId);
            }

            // 댓글 삭제 API 요청
            httpRequest("DELETE", "/api/comments/" + commentId, null, success, fail);
        }
    });
});



























