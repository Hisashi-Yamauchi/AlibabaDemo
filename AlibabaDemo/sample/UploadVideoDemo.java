import com.aliyun.vod.upload.impl.UploadImageImpl;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.*;
import com.aliyun.vod.upload.resp.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *次のJavaサンプルコードは、ビデオや画像をサポートするメディアタイプを使用して、サーバー側でオンデマンドでビデオにメディアファイルをアップロードする方法を示しています。
 *まず、動画アップロードでは現在、アップロードする4つの方法がサポートされています：
 * 1.ローカルファイルをアップロードし、シャードアップロードを使用し、ブレークポイントの再送信をサポートします（testUploadVideo関数を参照）。
 * 1.1ブレークポイントの再開がオフの場合、最大サポートアップロードタスク実行時間は3000秒です。
 *      特定のアップロードファイルサイズは、ネットワーク帯域幅とディスクの読み書き機能に関係します。
 * 1.2ブレークポイントを有効にすると、48.8TBの単一ファイルをサポートします。ブレークポイントを有効にした後、
 *      アップロードプロセス中に現在のアップロード場所がローカルディスクファイルに書き込まれ、ファイルのアップロードに影響します。
 *      速度、ファイルサイズに応じて開くかどうかを選択してください
 * 2.ネットワークストリームをアップロードし、アップロードするファイルのURLを指定したり、ブレークポイントのレジュームをサポートしたり、
 *      1つのファイルを最大5GBまでサポートしたりすることができます。 testUploadURLStream関数を参照してください。
 * 3.アップロードファイルストリーム、アップロードするローカルファイルを指定したり、ブレークポイントの再送信をサポートしたり、
 *      最大5GBの単一ファイルをサポートしたりすることができます。 testUploadFileStream関数を参照してください。
 * 4.ストリーミングアップロードは、アップロードの入力ストリームを指定することができます、
 *      ファイルストリームやネットワークストリームなどをサポートしています、ブレークポイントのレジュームをサポートしていない、
 *      単一ファイルの最大5GBをサポートしています。 testUploadStream関数を参照してください。
 *次に、画像のアップロードでは、現在2つのアップロード方法がサポートされています。
 * 1.ローカルファイルをアップロードする、ブレークポイントの再開をサポートしない、5GBの単一ファイルの最大サポート、testUploadImageLocalFile関数を参照
 * 2.ファイルストリームとネットワークストリームのアップロード、InputStreamパラメータは必須、ブレークポイントの再開はサポートされておらず、最大5GBの単一ファイルをサポートします。 testUploadImageStream関数を参照してください。
 *注：画像のアップロードが完了すると、ピクチャのピクチャIDとアドレスを返すだけでなく、情報GetImageInfo照会写真、インタフェースのマニュアルを参照してくださいhttps://help.aliyun.com/document_detail/89742.html
 * <p>
 *この例のオプションのパラメータは、設定する必要がない場合は無効なパラメータ値が期待通りにならないように削除してください。
 */

public class UploadVideoDemo {
    //アカウントAK情報を入力してください（必須）
    private static final String accessKeyId = "";
    //アカウントAK情報を入力してください（必須）
    private static final String accessKeySecret = "";

    public static void main(String[] args) {
        //One、ビデオファイルのアップロード
        //ビデオタイトル（必須）
        String title = "テストタイトル";
        // 1。ローカルファイルをアップロードしてファイルストリームをアップロードするとき、ファイル名はアップロードされたファイルの絶対パスです
        //      ：/ User / sample / file name.mp4（必須）
        // 2.ネットワークストリームがアップロードされると、ファイル名はファイル名.mp4（必須）などのソースファイル名になります。
        // 3.ストリーミングする場合、ファイル名はファイルname.mp4（必須）などのソースファイル名です。
        //アップロードメソッドのファイル名には拡張子が含まれている必要があります
        String fileName = "テストファイル名.mp4";

        //ローカルファイルアップロード
        testUploadVideo(accessKeyId, accessKeySecret, title, fileName);
        //アップロードする動画のネットワークストリームアドレス
        String url = "http://video.sample.com/sample.mp4";

        // 2。ネットワークストリームのアップロード
        // testUploadURLStream(accessKeyId, accessKeySecret, title, fileName, url);
        // 3。ファイルストリームのアップロード
        // testUploadFileStream(accessKeyId, accessKeySecret, title, fileName);
        // 4.ストリーミングアップロード（ファイルストリームやネットワークストリームなど）
        InputStream inputStream = null;
        //4.1 文件流
//        try {
//            inputStream = new FileInputStream(fileName);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        //4.2 网络流
//        try {
//            inputStream = new URL(url).openStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //testUploadStream(accessKeyId, accessKeySecret, title, fileName, inputStream);

        //二、图片上传

        //1.图片上传-本地文件上传
        //testUploadImageLocalFile(accessKeyId, accessKeySecret);

        //2.图片上传-流式上传(文件流和网络流)
        //testUploadImageStream(accessKeyId, accessKeySecret);
    }

    /**
     * 本地文件上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /*シャードがアップロードされるときの各シャードのサイズを指定します。デフォルトは1Mバイトです*/
        request.setPartSize(1 * 1024 * 1024L);
        /*は、スライスをアップロードするときの同時スレッドの数を指定できます。デフォルトは1です
        （注：この設定はサーバーのCPUリソースを占有し、サーバーに合わせて指定する必要があります）*/
        request.setTaskNum(1);
        /* ブレークポイントレジュームを有効にするかどうかにかかわらず、デフォルトのブレークポイントレジューム機能はオフになっています。
        ネットワークが不安定になったり、プログラムがクラッシュしたりすると、同じアップロード要求が再び開始され、未完了のアップロードタスクを続行できます。
        これは、3000秒のタイムアウト後にアップロードできない大きなファイルに適用されます。注意：ブレークポイントの再開が有効になっていると、
        アップロードプロセス中にアップロード場所がローカルディスクファイルに書き込まれ、ファイルのアップロード速度に影響します。
        実際の状況に応じて有効にするかどうかを選択してください */
        request.setEnableCheckpoint(false);
        /* OSSの低速要求ログの印刷タイムアウト期間は、アップロード時間がしきい値を超えたときにデバッグログが出力されることを意味します。
        このログをマスクする場合は、しきい値を調整します。 単位：ミリ秒、デフォルトは300,000ミリ秒*/
        //request.setSlowRequestsThreshold(300000L);
        /* 各シャードが遅いときにログを出力する時間のしきい値を指定します。デフォルトは300秒です*/
        //request.setSlowRequestsThreshold(300000L);
        /* デフォルトの透かしを使用するかどうか（オプション）、テンプレートグループIDを指定するときは、
        テンプレートグループの設定に従ってデフォルトの透かしを使用するかどうかを決定する */
        //request.setIsShowWaterMark(true);
        /* アップロード後にコールバックURLを設定する（オプション）オンデマンドコンソールを使用して
        メッセージリスンイベントを設定することをお勧めしますhttps://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /*ビデオカテゴリID（オプション）*/
        //request.setCateId(0);
        /*コンマで区切られたビデオタグ（オプション）* /
        //request.setTags("标签1,标签2");
        /*動画の説明（オプション）*/
        //request.setDescription("视频描述");
        /*表紙イメージ（オプション）*/
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /*テンプレートグループID（オプション）*/
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /*記憶領域（オプション）*/
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //オンデマンドサービスを要求するためのリクエストID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 設定されたコールバックURLが無効で、動画のアップロードに影響しない場合は、VideoIdを返してエラーコードを返すことができます。
            それ以外の場合は、アップロードに失敗した場合、VideoIdは空です。この場合、返されたエラーコードに従って特定のエラー理由を分析する必要があります。 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    /**
     * ネットワークストリームアップロードインターフェイス
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param url
     */
    private static void testUploadURLStream(String accessKeyId, String accessKeySecret, String title, String fileName, String url) {
        UploadURLStreamRequest request = new UploadURLStreamRequest(accessKeyId, accessKeySecret, title, fileName, url);
        /* 既定の透かし（オプション）を使用するかどうか、テンプレートグループIDを指定するときは、テンプレートグループ構成に従って既定の透かしを使用するかどうかを決定します*/
        //request.setShowWaterMark(true);
        /* アップロード後にコールバックURLを設定します（オプション）。
        オンデマンドコンソールでメッセージリスンイベントを設定することをお勧めします。
        https://help.aliyun.com/document_detail/57029.htmlのドキュメントを参照してください。
        */
        //request.setCallback("http://callback.sample.com");
        /* ビデオ分類ID（オプション） */
        //request.setCateId(0);
        /* コンマで区切られた動画タグ（オプション） */
        //request.setTags("标签1,标签2");
        /* 動画の説明（オプション） */
        //request.setDescription("视频描述");
        /* 表紙画像（オプション） */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* テンプレートグループID（オプション） */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 保管場所（オプション） */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadURLStreamResponse response = uploader.uploadURLStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n"); //オンデマンドサービスを要求するためのリクエストID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 設定されたコールバックURLが無効で、動画のアップロードに影響しない場合は、VideoIdを返してエラーコードを返すことができます。
            それ以外の場合は、アップロードに失敗した場合、VideoIdは空です。この場合、返されたエラーコードに従って特定のエラー理由を分析する必要があります。 
            */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    /**
     * ファイルストリームアップロードインターフェイス
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private static void testUploadFileStream(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadFileStreamRequest request = new UploadFileStreamRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadFileStreamResponse response = uploader.uploadFileStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n"); //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    /**
     * ストリームアップロードインターフェイス
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param inputStream
     */
    private static void testUploadStream(String accessKeyId, String accessKeySecret, String title, String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
         /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    /**
     * 画像アップロードインターフェース、ローカルファイルアップロードの例
     * 参数参考文档 https://help.aliyun.com/document_detail/55619.html
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    private static void testUploadImageLocalFile(String accessKeyId, String accessKeySecret) {
        /* 图片类型（必选）取值范围：default（默认)，cover（封面），watermark（水印）*/
        String imageType = "cover";
        /* 图片文件扩展名（可选）取值范围：png，jpg，jpeg */
        // String imageExt = "png";
        /* 图片标题（可选）长度不超过128个字节，UTF8编码 */
        // String title = "图片标题";
        /* 图片标签（可选）单个标签不超过32字节，最多不超过16个标签，多个用逗号分隔，UTF8编码 */
        // String tags = "标签1,标签2";
        /* 存储区域（可选）*/
        // String storageLocation = "out-4f3952f78c0211e8b3020013e7.oss-cn-shanghai.aliyuncs.com";
        /* 本地文件上传时，fileName为上传文件绝对路径，如:/User/sample/文件名称.png (必选)*/
        String fileName = "测试文件名称.png";

        UploadImageRequest request = new UploadImageRequest(accessKeyId, accessKeySecret, imageType);
        request.setFileName(fileName);
        UploadImageImpl uploadImage = new UploadImageImpl();
        UploadImageResponse response = uploadImage.upload(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");
        System.out.print("ErrorCode=" + response.getCode() + "\n");
        System.out.print("ErrorMessage" + response.getMessage() + "\n");
        System.out.print("ImageId=" + response.getImageId() + "\n");
        System.out.print("ImageURL=" + response.getImageURL() + "\n");

    }

    /**
     * 画像アップロードインターフェース、
     * ストリーミングアップロードの例（ファイルストリームとネットワークストリームをサポート）
     * 参数参考文档 https://help.aliyun.com/document_detail/55619.html
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    private static void testUploadImageStream(String accessKeyId, String accessKeySecret) {
        /* 图片类型（必选）取值范围：default（默认)，cover（封面），watermark（水印）*/
        String imageType = "cover";
        /* 图片文件扩展名（可选）取值范围：png，jpg，jpeg */
        // String imageExt = "png";
        /* 图片标题（可选）长度不超过128个字节，UTF8编码 */
        // String title = "图片标题";
        /* 图片标签（可选）单个标签不超过32字节，最多不超过16个标签，多个用逗号分隔，UTF8编码 */
        // String tags = "标签1,标签2";
        /* 存储区域（可选）*/
        // String storageLocation = "out-4f3952f78c0211e8b3020013e7.oss-cn-shanghai.aliyuncs.com";
        // 流式上传时，InputStream为必选，fileName为源文件名称，如:文件名称.png(可选)
        String fileName = "测试文件名称.png";

        UploadImageRequest request = new UploadImageRequest(accessKeyId, accessKeySecret, imageType);

        // 1.文件流上传
        // InputStream fileStream = getFileStream(request.getFileName());
        // if (fileStream != null) {
        //     request.setInputStream(fileStream);
        // }

        // 2.网络流上传
        // String url = "http://image.sample.com/sample.png";
        // InputStream urlStream = getUrlStream(url);
        // if (urlStream != null) {
        //     request.setInputStream(urlStream);
        // }

        // 开始上传图片
        UploadImageImpl uploadImage = new UploadImageImpl();
        UploadImageResponse response = uploadImage.upload(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");
        System.out.print("ErrorCode=" + response.getCode() + "\n");
        System.out.print("ErrorMessage" + response.getMessage() + "\n");
        System.out.print("ImageId=" + response.getImageId() + "\n");
        System.out.print("ImageURL=" + response.getImageURL() + "\n");
    }

    private static InputStream getFileStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream getUrlStream(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}