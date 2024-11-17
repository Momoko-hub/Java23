## はじめに
2024年7月から主にJavaとSpringBootを使用したアプリケーション開発について学習してきました。
その中で学んだことを活かしながらポートフォリオとして簡単なアプリケーションを作成しました。

## 概要
受講生管理システム。
受講生と受講生コース情報を管理するアプリケーションです。

## 機能一覧
|  | 機能一覧 |
| --- | --- |
| 1 | 受講生情報登録機能 |
| 2 | 受講生情報変更・削除機能 |
| 3 | 受講生一覧表示機能 |
| 4 | 受講生の条件検索機能 |
| 5 | 申込状況の更新機能 |
## 作成背景
アプリケーションの作成にあたり、REST APIやMySQLを使用して理解を深めていく。
基本的な実装を習得し今後のアプリケーション開発に繋げていく。

## 開発環境
・言語
　　Java21
・フレームワーク
　　Spring Boot  version3.3.2
・データベース
　　MySQL MySQL 3.0.3
・バージョン管理
　　GitHub
・インフラ環境
　　AWS（VPC,EC2,RDS）

## 設計書
### ER図
<img width="572" alt="スクリーンショット 2024-10-21 13 42 42" src="https://github.com/user-attachments/assets/36b70136-fe4a-499e-95c0-18bbbe18b2a5">

### DBのテーブル設計
<img width="1092" alt="スクリーンショット 2024-11-05 6 13 20" src="https://github.com/user-attachments/assets/2dbe4351-0f98-4487-94d3-98547b9b7bc5">


### URL設計書
<img width="886" alt="スクリーンショット 2024-11-04 16 58 20" src="https://github.com/user-attachments/assets/a0ef1e1c-6b6c-4929-820a-8189a0df5d45">

## 使用イメージ
Postmanを使用してリクエストをパラメータで送り動作させています。

### 受講生登録
<img width="1470" alt="スクリーンショット 2024-11-17 13 43 52" src="https://github.com/user-attachments/assets/1d97a0c3-2792-4b32-95a1-6913dfb2f33f">

### 受講生一覧検索
<img width="1470" alt="スクリーンショット 2024-11-17 13 41 51" src="https://github.com/user-attachments/assets/e27fb34d-a9da-4096-9217-d0275eec20c9">
<img width="1470" alt="スクリーンショット 2024-11-17 13 52 44" src="https://github.com/user-attachments/assets/ced753dc-3b34-4341-88f8-274bd0b00b7e">
※一部省略

### 受講生のID検索
<img width="1470" alt="スクリーンショット 2024-11-17 13 44 41" src="https://github.com/user-attachments/assets/45f09501-1f95-43a8-b912-6b61d8786534">

### 受講生の名前検索
<img width="1470" alt="スクリーンショット 2024-11-17 13 45 23" src="https://github.com/user-attachments/assets/daee8938-736d-4d13-9356-39a071a46844">

### 受講生情報の変更
<img width="1470" alt="スクリーンショット 2024-11-17 13 48 13" src="https://github.com/user-attachments/assets/84049f21-ccfd-4e06-93dd-5a78b4aa8461">

### 申込状況の更新
<img width="1470" alt="スクリーンショット 2024-11-17 13 49 28" src="https://github.com/user-attachments/assets/2f28130e-d1a6-4642-8e72-5a5837fd04e6">

## 工夫した点
・表示した時に分かりやすいようにコース情報の中に申込状況の項目を追加し、そのコースのステータスが一目でわかるように設計しました。

・repository,service,controllerの順に設計しそれぞれの役割を果たせるようにしました。

・エンドポイントやメソッド名で内容が分かるように名前付を意識しました。

## 苦労した点・反省点
・最初コードを書いた時には検索条件ごとにエンドポイントを設定していたが、クエリパラメータを使って検索できるように修正した。
→今回はやりながらの修正になってしまったため、今後は設計の時点からエンドポイントの意図を明確にしていきたいです。

・ローカル環境では実行できたが、EC2を使用すると500エラーになってしまった。

・テストが失敗してしまう時の原因追及。

## 今後の課題
・フロントエンドの実装

・これらの登録、更新、検索機能を使用したシフト管理アプリケーションを作成したい

　　
