package org.jumin.sample.zk.blob.utils

class BlobStorageTestVariables {

    companion object {
        val STORAGE_ACCOUNT_NAME: String = System.getProperty("", "")

        val CONTAINER_NAME: String = System.getProperty("", "")

        val CONTAINER_SAS_KEY: String = System.getProperty("", "")

        fun getContainerSasUrl() =
            "https://${STORAGE_ACCOUNT_NAME}.blob.core.windows.net/${CONTAINER_NAME}?${CONTAINER_SAS_KEY}"

    }
}