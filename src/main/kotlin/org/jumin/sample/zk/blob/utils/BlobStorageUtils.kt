package org.jumin.sample.zk.blob.utils

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder


class BlobStorageUtils {

    companion object {

        /**
         * Get Client to a Blob Storage Container. A Blob Storage Container is storage of the Blobs. It is a child from a Blob Service.
         *
         * @param blobSasUrl is the SAS url provided when generating the SAS Key. It does not any container information
         **/
        fun getBlobContainerClient(blobContainerSasUrl: String): BlobContainerClient =
            BlobContainerClientBuilder().endpoint(blobContainerSasUrl).buildClient()

        /**
         * Get Client to a Blob Storage Service. A Blob Storage Service is a parent of Blob containers.
         *
         * @param blobSasUrl is the SAS url provided when generating the SAS Key. It does not any container information
         **/
        fun getBlobServiceClient(blobSasUrl: String): BlobServiceClient =
            BlobServiceClientBuilder().endpoint(blobSasUrl).buildClient()

    }
}