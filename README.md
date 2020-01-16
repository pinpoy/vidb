# vidb
Accessibility机制实现模拟点击（微信自动抢红包实现）
先来讲述以下原理，原理很简单，当页面发生变化的时候，搜索屏幕中的关键字，搜索到了就点开。这个原理在微信旧版本的自动抢红包是有效的，但是在新版本中，开红包的界面没办法这么做了，那我们需要自己模拟点击动作，从而点开这个红包。
AccessibilityService 在 SDK24 (即Android 7.0) 引入了一个方法 dispatchGesture ，从而使我们可以不用 root 就能模拟点击动作。所以在 app/build.gradle 中我们将最小的 SDK 版本设置为 24( minSdkVersion 24)，所以如果你的安卓手机系统小于安卓 7.0，那就没办法使用我这个抢红包方法了。

#核心service

```java
public class EnvelopeService extends AccessibilityService {
    
    @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
            final int eventType = event.getEventType();
    
            //通知栏事件
            if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                 //通过拦截通知事件模拟点击，打开app，根据控件查找红包，再次点击打开红包
                 //细节请看code
            }
    }
}



```

