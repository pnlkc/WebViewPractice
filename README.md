# WebViewPractice
WebView 사용 연습을 위한 프로젝트입니다.
<br>

## 230503

### Accompanist Webview로 Compose에서 WebVeiw 사용하는 방법 정리
- Accompanist Webview는 구글에서 개발했지만 구글 공식 라이브러리는 아닙니다.
<br>

**1. 모듈 수준의 build.gradle 파일에 dependency 추가**
- `implementation "com.google.accompanist:accompanist-webview:0.31.1-alpha"` 추가합니다.
<br>

**2. Manifest 파일에 인터넷 권한 추가**
- `<uses-permission android:name="android.permission.INTERNET" />` 추가합니다.
- 추가로 http 링크를 접속하기 위해서는 Manifest 파일의 application 단락에 `android:usesCleartextTraffic="true"`를 추가해야 합니다.
<br>

**3. WebView Composable을 생성**
- com.google.accompanist.web.WebView Composable을 만들어 사용합니다.
- 아래는 예시 코드입니다.
``` kotlin
// WebView의 State를 저장하는 remember 객체 입니다
val webViewState = rememberWebViewState(
    url = text.value,
    additionalHttpHeaders = emptyMap()
)

// Accompanist Webview는 AccompanistWebViewClient, AccompanistWebChromeClient를 사용해야 합니다
// WebViewClient는 WebView의 요청을 처리하고, 페이지를 로드하고, 에러 상황을 처리하는 데 사용되는 클래스입니다
val webViewClient = object: AccompanistWebViewClient() {
    // onPageStarted()는 페이지 로드가 시작될 때 호출됩니다
    // 페이지 로드가 완료되면 onPageFinished() 메서드가 호출됩니다
    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        // url을 text 변수에 저장하면 현재 로드되고 있는 페이지의 url을 가져와 사용할 수 있습니다
        text.value = url ?: ""
        super.onPageStarted(view, url, favicon)
    }
}
// 일반적으로 WebChromeClient를 사용하면 웹 페이지에서 일어나는 일부 이벤트에 대해 처리할 수 있습니다
// 예를 들어, onReceivedTitle 메서드를 사용하여 웹 페이지의 타이틀을 가져올 수 있습니다
val webChromeClient = AccompanistWebChromeClient()


// Accompanist WebView Composable입니다
WebView(
    state = webViewState,
    client = webViewClient,
    chromeClient = webChromeClient,
    onCreated = { webView ->
        with(webView) {
            settings.run {
                // WebView에서 자바스크립트 사용을 허용합니다
                javaScriptEnabled = true
                // WebView에서 DOM 스토리지를 사용할 수 있도록 합니다
                // DOM 스토리지는 웹 페이지에서 로컬로 데이터를 저장하고 검색하는 데 사용됩니다
                domStorageEnabled = true
                // WebView에서 자바스크립트로 새 창을 자동으로 열 수 있는지 여부를 결정합니다
                javaScriptCanOpenWindowsAutomatically = false
            }
        }
    },
    // 뒤로가기 버튼으로 웹페이지를 뒤로 가는 기능을 활성화 시키는 코드입니다
    captureBackPresses = true,
    modifier = modifier
        .fillMaxSize(),
)
```
