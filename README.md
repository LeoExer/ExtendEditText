# ExtendEditText

ExtendEditText是基于EditText的文本编辑器, 只要一行代码就更改文本的样式.

![demo.gif](./gif/demo.gif "demo.gif")

## Quick Setup
#### 1.添加依赖
```
compile 'com.leo.extendedittext:library:0.1.1'
```

#### 2.布局中配置
```
<com.leo.extendedittext.ExtendEditText
	android:id="@+id/extend_edit_text"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:paddingLeft="@dimen/activity_horizontal_margin"
	android:paddingRight="@dimen/activity_horizontal_margin"
	android:textSize="@dimen/normal_text_size"
	android:scrollbars="none"
	android:background="@android:color/transparent"
	app:bulletColor="@color/colorPrimary" // 着重号颜色
	app:bulletRadius="@dimen/bullet_radius" // 着重号半径
	app:bulletGapWidth="@dimen/bullet_gap_width" // 着重号与文本的宽度
	app:quoteColor="@color/colorPrimary" // 引用颜色
	app:quoteStripeWidth="@dimen/quote_stripe_width" // 引用宽度
	app:quoteGapWidth="@dimen/quote_gap_width" // 引用与文本的宽度
	app:linkColor="@color/colorPrimaryDark" // 链接颜色
	app:drawUnderLine="true" // 链接是否画下划线
	app:enableHistory="true" // 是否开启历史记录
	app:historyCapacity="50" // 历史记录容量
	app:rule="EXCLUSIVE_EXCLUSIVE"> // 规则，后面说
</com.leo.extendedittext.ExtendEditText>
```

#### 3.设置样式
选中文本，调用相应的接口，所选文本就会更换样式。
```
mExtendEdt.bold(); // 粗体

mExtendEdt.italic(); // 斜体

mExtendEdt.underline(); // 下划线

mExtendEdt.strikethrough(); // 删除线

mExtendEdt.link(); // 链接

mExtendEdt.bullet(); // 着重号

mExtendEdt.quote(); // 引用
```

## Support
- **bold**
- *italic*
- <u>underline</u>
- <s>strikethrough</s>
- [link](https://github.com/LeoExer/ExtendEditText)
- - bullet
- >quote

## TODO
- insert image
- background color

## Reference
- [Spans，一个强大的概念](https://rocko.xyz/2015/03/04/%E3%80%90%E8%AF%91%E3%80%91Spans%EF%BC%8C%E4%B8%80%E4%B8%AA%E5%BC%BA%E5%A4%A7%E7%9A%84%E6%A6%82%E5%BF%B5/#使用自定义的span)。
- [Spanned | Android Developers](http://developer.android.com/reference/android/text/Spanned.html)
- [Spannable | Android Developers](https://developer.android.com/reference/android/text/Spannable.html)
- [Knife | github](https://github.com/mthli/Knife)

## License
```
Copyright (C) 2017 Leo Wu
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
    
