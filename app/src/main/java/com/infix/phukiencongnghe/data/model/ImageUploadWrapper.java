package com.infix.phukiencongnghe.data.model;

import android.net.Uri;

/**
 * Lớp này có nhiệm vụ bọc thông tin
 * Ví dụ:
 *      + Đối với ảnh chính: type=MAIN
 *      + Đối với ảnh phụ của sản phẩm chung: type=SUB
 *      + Đối với ảnh biến thể: type=VARIANT, lúc này cần có thêm variantSku để truy ngược về đối tượng biến thể
 *      + Với thuộc tính type, dựa vào đó để xác định url download trả về là loại nào để gán url download tương ứng cho
 *      lớp ProductAdminPageDTO
 */
public class ImageUploadWrapper {
    private Uri localUri;
    private String storagePath;
    private String type;
    private String variantSku;

    public ImageUploadWrapper(Uri localUri, String storagePath, String type, String variantSku) {
        this.localUri = localUri;
        this.storagePath = storagePath;
        this.type = type;
        this.variantSku = variantSku;
    }

    public Uri getLocalUri() { return localUri; }
    public String getStoragePath() { return storagePath; }
    public String getType() { return type; }
    public String getVariantSku() { return variantSku; }
}