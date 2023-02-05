package org.jumin.sample.zk.blob.utils

import com.azure.storage.blob.models.ListBlobsOptions
import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test

/**
 * Don't forget to assign the Storage Account Name, Blob Container Name and Container SAS Key
 */
//@Test
fun getBlobContainerClient() {
    val containerClient = BlobStorageUtils.getBlobContainerClient(BlobStorageTestVariables.getContainerSasUrl())
    Assertions.assertNotNull(containerClient)
}


class BlobStorageUtilsTester {

   // @Test
    fun listBlobContainerContents() {
        val containerClient = BlobStorageUtils.getBlobContainerClient(BlobStorageTestVariables.getContainerSasUrl())
        Assertions.assertNotNull(containerClient)

        containerClient.listBlobs()
            .forEach { blobItem ->
                println("Blob name: ${blobItem.name}, " +
                        "Blob Type: ${blobItem.properties.blobType}, " +
                        "Content Type: ${blobItem.properties.contentType}, " +
                        "Size: ${blobItem.properties.contentLength}, " +
                        "Modify Date: ${blobItem.properties.lastModified}, " +
                        "Prefix: ${blobItem.isPrefix}"
                )
            }

        println("\nList Blobs by Hierarchy")

        val dirList = mutableListOf<String>()
        containerClient.listBlobsByHierarchy("")
            .forEachIndexed { index, blobItem ->
                println("#${index} Blob name: ${blobItem.name}, " +
                        "Blob Type: ${blobItem.properties?.blobType}, " +
                        "Content Type: ${blobItem.properties?.contentType}, " +
                        "Size: ${blobItem.properties?.contentLength}, " +
                        "Modify Date: ${blobItem.properties?.lastModified}, " +
                        "Prefix: ${blobItem.isPrefix}"
                )

                if (blobItem.isPrefix) {
                    dirList.add(blobItem.name)
                }
            }

        dirList.forEach {
            println("Listing content of directory: $it")
            val delimiter = "/"
            val options: ListBlobsOptions = ListBlobsOptions().setPrefix("$it")
            containerClient.listBlobsByHierarchy(delimiter, options, null)
                .forEachIndexed { index, blobItem ->
                    println("#${index} Blob name: ${blobItem.name}, " +
                            "Blob Type: ${blobItem.properties?.blobType}, " +
                            "Content Type: ${blobItem.properties?.contentType}, " +
                            "Size: ${blobItem.properties?.contentLength}, " +
                            "Modify Date: ${blobItem.properties?.lastModified}, " +
                            "Prefix: ${blobItem.isPrefix}"
                    )

                }
        }
    }
}