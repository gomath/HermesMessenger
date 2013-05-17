/**
 * CONVERSATION VIEW VISUAL TESTING
 * The conversation view is where the majority of user interaction goes on.
 * The obvious check was the GUI was properly laid out, containing all the
 * correct components. The actions for each possible user interaction with
 * on-screen components are documented above in the “User Experience” section.
 * We verified that all of these actions are correct by printing out the
 * messages being passed between the server and client and checking if they
 * were correct. We did this for all messages in our Client/Server protocol
 * which includes logging in, logging out, sending a message, starting a
 * conversation, and closing a conversation. We tested this for all normal
 * uses and edge cases, such as trying to start a conversation when no users
 * are selected (or no one  else is online), and various combinations of
 * closing/opening/checking history of conversations. We checked concurrency
 * by running clients on different computers and simultaneously logging on with
 * the same username, logging off, starting conversations simultaneously, and
 * sending messages simultaneously. All of the behaviors remained normal in
 * all these cases.
 */
