package com.example.webviewpractice

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webviewpractice.ui.theme.WebViewPracticeTheme
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

// WebView를 사용하기 전에 Manifest 파일에서 Internet 권한 요청이 필요합니다.
// 추가로 http 링크를 접속하기 위해서는 Manifest 파일의 application 단락에
// android:usesCleartextTraffic="true" 를 추가해야 합니다.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebViewPracticeTheme {
                WebViewCompose()
            }
        }
    }
}

// com.google.accompanist:accompanist-webview 라이브러리 사용하였습니다.
// 구글에서 개발했지만 공식 라이브러리는 아닙니다.
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewCompose(
    modifier: Modifier = Modifier,
) {
    val text = remember { mutableStateOf("") }
    val tempText = remember { mutableStateOf("") }

    // WebView의 State를 저장하는 remember 객체 입니다.
    val webViewState = rememberWebViewState(
        url = text.value,
        additionalHttpHeaders = emptyMap()
    )

    // Accompanist Webview는 AccompanistWebViewClient, AccompanistWebChromeClient를 사용해야 합니다.

    // WebViewClient는 WebView의 요청을 처리하고, 페이지를 로드하고, 에러 상황을 처리하는 데 사용되는 클래스입니다.
    val webViewClient = object: AccompanistWebViewClient() {
        // onPageStarted()는 페이지 로드가 시작될 때 호출됩니다
        // 페이지 로드가 완료되면 onPageFinished() 메서드가 호출됩니다.
        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            tempText.value = url ?: ""
            super.onPageStarted(view, url, favicon)
        }
    }

    // 일반적으로 WebChromeClient를 사용하면 웹 페이지에서 일어나는 일부 이벤트에 대해 처리할 수 있습니다.
    // 예를 들어, onReceivedTitle 메서드를 사용하여 웹 페이지의 타이틀을 가져올 수 있습니다.
    val webChromeClient = AccompanistWebChromeClient()



    /*
    // 아래 방법처럼 수동으로 뒤로가기 기능을 추가할 수도 있습니다.
//    var backWait = 0L
//    val context = LocalContext.current
//
//    // 뒤로가기 제어용 webViewNavigator 변수
//    val webViewNavigator = rememberWebViewNavigator()
//
//    BackHandler {
//        if (webViewNavigator.canGoBack) {
//            webViewNavigator.navigateBack()
//        } else {
//            if (System.currentTimeMillis() - backWait >= 2000) {
//                backWait = System.currentTimeMillis()
//                Toast.makeText(context, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
//            } else {
//                (context as? Activity)?.finish()
//            }
//        }
//    }
     */

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = tempText.value,
            onValueChange = { textValue ->
                tempText.value = textValue
            },
            singleLine = true,
            trailingIcon = {
                Button(
                    onClick = {
                        text.value = tempText.value
                    },
                    modifier = modifier.padding(end = 10.dp)
                ) {
                    Text(
                        text = "검색"
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
        )

        WebView(
            state = webViewState,
            client = webViewClient,
            chromeClient = webChromeClient,
//            navigator = webViewNavigator,
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
            // 뒤로가기 버튼으로 웹페이지를 뒤로가는 기능을 활성화 시키는 코드입니다
            captureBackPresses = true,
            modifier = modifier
                .fillMaxSize(),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WebViewPracticePreview() {
    WebViewPracticeTheme {
        WebViewCompose()
    }
}