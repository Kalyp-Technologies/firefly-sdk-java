package com.kalyp.firefly;

import com.kalyp.firefly.api.DefaultNamespaceApi;
import com.kalyp.firefly.api.GlobalApi;
import com.kalyp.firefly.api.NonDefaultNamespaceApi;
import com.kalyp.firefly.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firefly {

    private DefaultNamespaceApi defaultNamespaceApi;
    private GlobalApi globalApi;
    private NonDefaultNamespaceApi nonDefaultNamespaceApi;

    private ApiClient apiClient;

    public Firefly(String uri) {
        this.apiClient = new ApiClient().setBasePath(uri);
        this.defaultNamespaceApi = new DefaultNamespaceApi(apiClient);
        this.globalApi = new GlobalApi(apiClient);
        this.nonDefaultNamespaceApi = new NonDefaultNamespaceApi(apiClient);
    }

    public void sendBroadcast(String message) {

        // Forward the broadcast on to firefly-core component.
        PostNewMessageBroadcastRequest ffRequest = new PostNewMessageBroadcastRequest()
                .addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message));
        PostNewMessageBroadcast200Response postNewMessageBroadcast200Response = defaultNamespaceApi.postNewMessageBroadcast(null, null, ffRequest);
    }

    public void sendPrivateMessage(String message, String recipient) {

        PostNewMessagePrivateRequest ffPrivateMessage = new PostNewMessagePrivateRequest()
                .addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message))
                .group(new PostContractAPIInvokeRequestMessageGroup()
                        .addMembersItem(new PostContractAPIInvokeRequestMessageGroupMembersInner()
                                .identity(recipient)));
        defaultNamespaceApi.postNewMessagePrivate(null, null, ffPrivateMessage);

    }
    public void sendPrivateMessageWithReply(String message) {

        PostNewMessagePrivateRequest ffPrivateMessage = new PostNewMessagePrivateRequest().addDataItem(new PostContractAPIInvokeRequestMessageDataInner().value(message));
        defaultNamespaceApi.postNewMessageRequestReply(null, ffPrivateMessage);

    }

    public List<Namespace> listNamespaces () {

        return globalApi.getNamespaces(null, null);

    }

    public List<Namespace> listNamespacesIncludeInitializing () {

        return globalApi.getNamespaces("true", null);

    }

    public List<Identity> listIdentities () {

        return defaultNamespaceApi.getIdentities(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    }

    public Identity getIdentityByIdWithVerifiers(String id) {

        return defaultNamespaceApi.getIdentityByID(id, "true", null);

    }

    public List<GetDatatypes200ResponseInner> listDataTypes() {

        return defaultNamespaceApi.getDatatypes(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public List<Identity> listOrganizations() {

        return defaultNamespaceApi.getNetworkOrgs(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    }

    // TODO implement methods as per firefly-node-sdk [__ ryan-griffiths 02-02-24]

    // TODO implement query and invoke using the FFI [__ ryan-griffiths 02-02-24]

    // TODO temporary to rewire up existing services [__ ryan-griffiths 05-02-24]
    //   - should refactor to match firefly-node-sdk (and not be Fabric specific)
    /** Invoke where chaincode args have already been serialised (i.e. don't require firefly FFI. */
    public PostContractAPIInvoke200Response invoke(String channel, String chaincode, String function, List<String> args, boolean confirm) {

        // Prepare the firefly core request for the `contracts/invoke` endpoint.
        PostContractInvokeRequest ffRequest = new PostContractInvokeRequest()
            .input(ffiInputs(args))
            .location(Map.of(
                "channel", channel,
                "chaincode", chaincode))
            .method(ffiMethod(function, args.size()))
            .methodPath(function);

        // Invoke the chaincode, return the result.
        // NOTE that this appears like fabconnect to: [#__ ryan-griffiths 07-12-23]
        //   - not include the chaincode result
        //   - nor include the tx hash unless sync is true.
        return defaultNamespaceApi.postContractInvoke(String.valueOf(confirm), null, ffRequest);
    }

    // TODO temporary to rewire up existing services [__ ryan-griffiths 05-02-24]
    //   - should refactor to match firefly-node-sdk (and not be Fabric specific)
    /** Query where chaincode args have already been serialised (i.e. don't require firefly FFI. */
    public Map<String, Object> query(String channel, String chaincode, String function, List<String> args) {

        // Prepare the firefly-core query API call.
        PostContractQueryRequest ffRequest = new PostContractQueryRequest()
            .input(ffiInputs(args))
            .location(Map.of(
                "channel", channel,
                "chaincode", chaincode))
            .method(ffiMethod(function, args.size()))
            .methodPath(function);

        // Call the API and return the chaincode result.
        return defaultNamespaceApi.postContractQuery(null, ffRequest);
    }

    // Create an FFI for a variable number of pre-serialised args.
    private PostContractInvokeRequestMethod ffiMethod(String method, int nArgs) {
        PostContractInvokeRequestMethod ffiMethod = new PostContractInvokeRequestMethod().name(method);
        for (int i = 0; i < nArgs; i++) {
            ffiMethod.addParamsItem(new GetContractAPIInterface200ResponseMethodsInnerParamsInner()
                .name("arg" + i)
                .schema(Map.of("type", "string")));
        }
        return ffiMethod;
    }

    // Format the (already ordered args) into an ordered map matching the FFI so they can be re-ordered later.
    private Map<String, Object> ffiInputs(List<String> args) {
        Map<String, Object> unorderedArgs = new HashMap<>();
        for (int i = 0; i < args.size(); i++) {
            unorderedArgs.put("arg" + i, args.get(i));
        }
        return unorderedArgs;
    }

    // TODO debug only? [__ ryan-griffiths 06-02-24]
    public ApiClient apiClient() {
        return apiClient;
    }

    public DefaultNamespaceApi defaultNamespaceApi(){
        return defaultNamespaceApi;
    }

    public GlobalApi globalApi(){
        return globalApi;
    }

    public NonDefaultNamespaceApi nonDefaultNamespaceApi(){
        return nonDefaultNamespaceApi;
    }

    public GetContractAPIListeners200ResponseInner registerContractListener(
        String listenerName,
        String channel,
        String chaincode,
        String eventName,
        String topic
    ) {

        // TODO obviously this is Fabric specific... testing only for now. [__ ryan-griffiths 08-02-24]
        //   - note: no need to have an FFI defined to listen to CC events.
        PostNewContractListenerRequest request = new PostNewContractListenerRequest()
            .name(listenerName)
            .location(Map.of(
                "channel", channel,
                "chaincode", chaincode))
            .event(new GetContractAPIListeners200ResponseInnerEvent().name(eventName))
            .options(new GetContractAPIListeners200ResponseInnerOptions().firstEvent("newest"))
            .topic(topic);

        return defaultNamespaceApi.postNewContractListener(null, request);
    }

    /**
     * POST http://localhost:5000/api/v1/namespaces/default/contracts/listeners
     *
     * {
     *   "event": {
     *     "name": ".*"
     *   },
     *   "location": {
     *     "channel": "firefly",
     *     "chaincode": "example"
     *   },
     *   "name": "subscriptionName",
     *   "options": {
     *     "firstEvent": "oldest"
     *   },
     *   "topic": "pe-events"
     * }
     */

    // https://hyperledger.github.io/firefly/tutorials/events.html#websockets-example-1-ephemeral-subscription-with-auto-commit
    //
    // To listen to events:
    // wscat -c 'ws://localhost:5000/ws?namespace=default&ephemeral&autoack'
    //
    // Alternatively, could also set up a subscription and then listen to that particular subscription.
    // POST http://localhost:5000/api/v1/namespaces/default/subscriptions
    // The subscription allows for filtering e.g.:
    //   - for a particular listener id: `{"filter": {"blockchainevent": { "listener": "f6d46424-8e05-4d6b-9dad-b4a8624a9d0c" }}}`
    //   - or a particular CC event key: `{"filter": {"blockchainevent": { "name": "PeApproveEvent" }}}`
}
