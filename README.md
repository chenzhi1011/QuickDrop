# QuickDrop - クロスデバイスファイル転送ツール
QuickDrop は、デバイス間でファイルを素早く共有できるシンプルなファイル転送アプリです。  
アップロードされたファイルは 30 分後に自動削除され、使い捨て感覚で安心して利用できます。

## アクセス

- デモサイト：［https://a-quickdrop.vercel.app/] (demo)

## 主な機能

- ドラッグ＆ドロップでファイルアップロード
- 一意のダウンロードリンクの自動生成
- 30 分後にファイルとメタ情報を自動削除（バックエンドスケジューラ）
- 複数端末間でのファイル受け渡しを簡単に実現
- レスポンシブ対応 UI（スマホ/タブレットでも快適）

## 使用技術

### フロントエンド
- HTML / CSS / JavaScript
- Vercel にデプロイ

### バックエンド
- Spring Boot (Java 17)
- H2 Database（一時的なローカル保存）
- REST API (`/upload`, `/download/{token}`)
- Render にデプロイ
- スケジューラーによる期限切れファイル削除
- CORS 対応済み（Vercel からのアクセスを許可）

## 📁 ディレクトリ構成（概要）
QuickDrop/  
├── src/  
│   └── main/  
│       ├── java/com/quickdrop/  
│       │   ├── controller/  
│       │   ├── service/  
│       │   ├── repository/  
│       │   ├── entity/  
│       │   └── scheduler/  
│       └── resources/  
│           └── application.yml  
├── Frontend/  
│   ├── index.html  
│   ├── style.css  
│   └── script.js  
├── Dockerfile  
├── README.md  


## 注意事項

- Render の無料プランでは、アップロードされたファイルや DB は永続保存されません。
- 実運用を想定する場合は、S3 などの外部ストレージや PostgreSQL 等の永続 DB に切り替えを検討してください。
- **Render 上のバックエンドは 15 分間アクセスがないと自動的に休眠状態になります。**  
  ➡️ そのため、初回アクセス時にはサーバーの起動に数秒〜十数秒かかる場合があります。


## 今後の改善アイデア

-  パスコード付きダウンロード
-  有効期限カスタマイズ
-  S3 へのアップロード対応
-  PWA 化によるスマホ対応強化
