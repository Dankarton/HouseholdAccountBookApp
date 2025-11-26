# 家計簿アプリ

このアプリは，日々の収支を記録しながら，購入日と支払日が異なる支払方法 (例:クレジットカード，後払いサービス)にも対応できる家計簿アプリです．
従来の家計簿では購入日ベースでしか残高を管理できないことが多いですが，本アプリでは実際の引き落とし日を反映することで，正確な時系列の残高を
確認できます．これにより，将来の支払予定を含めた実際の資金状況を把握でき，安心して家計管理を行うことが出来ます．   
本アプリでは，金銭のやり取りに関係なく購入した日や契約した日を支出日，実際に支払いが行われた日を支払日として扱っています．  

## 主な機能
- **収支の記録**  
  日々の収入・支出をカテゴリー別に登録可能です．支払方法を選択することで，支出日と支払日が自動的に入力されます．  
- **月次データの一覧表示**  
  1か月ごとのデータを日付順に表示します．残高に加えて翌月以降の支払金額も確認可能です．  
- **円グラフ**  
  支出・収入のカテゴリ別割合を円グラフで表示します．  
- **データの編集・削除**  
  一覧表示画面から支出データの編集・削除が可能です．  
- **カテゴリ・支払方法の管理**    
  設定画面で支出・収入カテゴリや支払方法の追加・編集・削除が可能です．  
- **初期残高の設定**  
  初期残高を設定することで，正確な残高計算を行えるようになります．

## 画面遷移構成
<pre>
MainActivity --- 4つの子Fragmentを下タブで切り替え  
    ┣━ InputMotherFragment --- 2の子Fragmentを上タブで切り替え  
    ┃   ┣━ PurchaseEditFragment --- 支出入力画面  
    ┃   ┗━ IncomeEditFragment --- 収入入力画面  
    ┣━ TransactionDataListFragment --- 月次データ一覧表示画面   
    ┣━ ChartMotherFragment --- 月次データ円グラフ表示画面  
    ┗━ SettingsMenuFragment --- 設定画面  
         ┣━ SettingMotherAcvitity  
         ┃    ┗━ BalanceEditFragment  
         ┃         ┗━ SettingBalanceEditActivity  
         ┣━ SettingSelectPurchaseCategoryActivity  
         ┃    ┗━ BaseListingFragment --- 一覧表示されている支出カテゴリをタップすることで編集画面に遷移  
         ┃         ┗━ SettingEditPurchaseCategoryActivity  
         ┣━ SettinbSelectIncomeCategoryActivity  
         ┃    ┗━ BaseListingFragment --- 一覧表示されている収入カテゴリをタップ編集画面に遷移  
         ┃         ┗━ SettingEditIncomeCategoryActivity  
         ┗━ SettincSlectPaymentMethodActivity  
              ┗━ BaseListingFragment --- 一覧表示されている支払方法をタップすることで編集画面に遷移  
                   ┗━ SettingBalanceEditActivity  
</pre>
  
## 開発環境
- Android Studio
- Gradle 8.13
- Java 17
- minSdkVersion 27
- targetSdkVersion 33

実機でのテストはSdkVersion35でしか行っていません．
