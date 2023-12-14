package com.kalyp.firefly;

import com.kalyp.firefly.api.DefaultNamespaceApi;
import com.kalyp.firefly.api.GlobalApi;
import com.kalyp.firefly.api.NonDefaultNamespaceApi;
import com.kalyp.firefly.model.*;

import java.util.List;

public class Firefly {

    private DefaultNamespaceApi defaultNamespaceApi;
    private GlobalApi globalApi;
    private NonDefaultNamespaceApi nonDefaultNamespaceApi;

    public Firefly(String host) {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(host);
        this.defaultNamespaceApi = new DefaultNamespaceApi(apiClient);
        this.globalApi = new GlobalApi(apiClient);
        this.nonDefaultNamespaceApi = new NonDefaultNamespaceApi(apiClient);
    }

    public void sendBroadcast(String message) throws ApiException {

        // Forward the broadcast on to firefly-core component.
        PostNewMessageBroadcastRequest ffRequest = new PostNewMessageBroadcastRequest()
                .addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message));
        PostNewMessageBroadcast200Response postNewMessageBroadcast200Response = defaultNamespaceApi.postNewMessageBroadcast(null, null, ffRequest);
    }

    public void sendPrivateMessage(String message, String recipient) throws ApiException {

        PostNewMessagePrivateRequest ffPrivateMessage = new PostNewMessagePrivateRequest()
                .addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message))
                .group(new PostContractAPIInvokeRequestMessageGroup()
                        .addMembersItem(new PostContractAPIInvokeRequestMessageGroupMembersInner()
                                .identity(recipient)));
        defaultNamespaceApi.postNewMessagePrivate(null, null, ffPrivateMessage);

    }
    public void sendPrivateMessageWithReply(String message) throws ApiException {

        PostNewMessagePrivateRequest ffPrivateMessage = new PostNewMessagePrivateRequest().addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message));
        defaultNamespaceApi.postNewMessageRequestReply(null, ffPrivateMessage);

    }

    public List<Namespace> listNamespaces () throws ApiException {

        return globalApi.getNamespaces(null, null);

    }

    public List<Namespace> listNamespacesIncludeInitializing () throws ApiException {

        return globalApi.getNamespaces("true", null);

    }

    public List<Identity> listIdentities () throws ApiException {

        return defaultNamespaceApi.getIdentities(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    }

    public Identity getIdentityByIdWithVerifiers(String id) throws ApiException {

        return defaultNamespaceApi.getIdentityByID(id, "true", null);

    }

    public List<GetDatatypes200ResponseInner> listDataTypes() throws ApiException {

        return defaultNamespaceApi.getDatatypes(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public List<Identity> listOrganizations() throws ApiException {

        return defaultNamespaceApi.getNetworkOrgs(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    }
}
