package com.calclab.examplechat.client;

import java.util.Date;
import java.util.List;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.extend.UIExtensionPointManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.chat.ChatDefault;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogPlugin;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatSubjectParam;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatExampleEntryPoint implements EntryPoint {
    private Button btnLogin;
    private Button btnLogout;
    private Button btnSamplesExtUI;
    private DefaultDispatcher dispatcher;
    private PasswordTextBox passwordInput;
    private Presence presenceForTest;
    private TextBox resourceInput;
    private TextBox userNameInput;
    private Xmpp xmpp;

    public void onModuleLoad() {
        /*
         * Install an UncaughtExceptionHandler which will produce <code>FATAL</code>
         * log messages
         */

        /*
         * Currently we let firebug to catch the error:
         * Log.setUncaughtExceptionHandler();
         */

        // At the moment, in runtime:
        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);

        Log.getDivLogger().moveTo(10, 60);
        Log.getDivLogger().setSize("1000", "400");

        /*
         * Use a deferred command so that the UncaughtExceptionHandler catches
         * any exceptions in onModuleLoadCont()
         */
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                onModuleLoadCont();
            }
        });
    }

    public void onModuleLoadCont() {

        createPresenceForTest();

        createInterface();
        createExtUI();

        this.xmpp = Xmpp.create(new BoshOptions("http-bind", "localhost"));

        xmpp.getSession().addListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                Log.info("STATE CHANGED: " + current + " - old: " + old);
                switch (current) {
                case connected:
                    btnLogin.setEnabled(false);
                    btnLogout.setEnabled(true);
                    dispatcher.fire(ChatDialogPlugin.SET_STATUS, MultiChatView.STATUS_ONLINE);
                    break;
                case connecting:
                    btnLogin.setEnabled(false);
                    break;
                case disconnected:
                    btnLogin.setEnabled(true);
                    btnLogout.setEnabled(false);
                    dispatcher.fire(ChatDialogPlugin.SET_STATUS, MultiChatView.STATUS_OFFLINE);
                    break;
                }
            }
        });

        xmpp.getRoster().addListener(new RosterListener() {
            public void onRosterInitialized(final List<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    Log.info("Rooster, adding: " + item.getXmppURI() + " name: " + item.getName() + " subsc: "
                            + item.getSubscription());
                    dispatcher.fire(ChatDialogPlugin.ADD_PRESENCE_BUDDY, new PairChatUser("images/person-def.gif", item
                            .getXmppURI(), item.getXmppURI().toString(), "maroon", presenceForTest));
                }
            }
        });

        xmpp.getChat().addListener(new ChatManagerListener() {
            public void onChatCreated(final ChatDefault chatDefault) {
                dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, chatDefault);

                dispatcher.subscribe(ChatDialogPlugin.ON_MESSAGE_SENDED, new Action<ChatMessageParam>() {
                    public void execute(final ChatMessageParam param) {
                        // xmpp.send(param.getTo().toString(),
                        // param.getMessage());
                    }
                });

                // chat.
                // final ChatPanel chatPanel =
                // conversationsPanel.createChat(chat.getID(), new
                // ChatPanelListener() {
                // public void onSend(ChatPanel source, String text) {
                // chat.send(text);
                // source.clearMessage();
                // }
                //
                // });
                // chat.addListener(new ChatListener() {
                // public void onMessageReceived(final Chat chat, final Message
                // message) {
                // chatPanel.showIcomingMessage(message.getFrom(),
                // message.getBody());
                // }
                //
                // public void onMessageSent(final Chat chat, final Message
                // message) {
                // chatPanel.showOutMessage(message.getBody());
                // }
                // });
                // chatPanel.clearMessage();
            }
        });

        xmpp.getChat().addListener(new ChatManagerListener() {
            public void onChatCreated(final ChatDefault chatDefault) {
                chatDefault.addListener(new ChatListener() {
                    public void onMessageReceived(final ChatDefault chatDefault, final Message message) {
                        // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED,
                        // new ChatMessageParam(XmppURI.parse(message
                        // .getFrom()), XmppURI.parse(message.getTo()),
                        // message.getBody()));
                    }

                    public void onMessageSent(final ChatDefault chatDefault, final Message message) {
                    }

                });
            }

        });

        xmpp.getPresenceManager().addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                Log.debug("PRESENCE: " + presence.getFromURI());
            }

            public void onSubscriptionRequest(final Presence presence) {
                Log.debug("SUBSCRIPTION REQUEST: " + presence);
            }
        });

    }

    private void chatSamples() {
        // MultiChatSamples.show(dispatcher, userNameInput.getText());
    }

    private HorizontalPanel createButtonsPane() {
        final HorizontalPanel buttons = new HorizontalPanel();
        btnLogin = new Button("Login", new ClickListener() {
            public void onClick(final Widget source) {
                login();
            }
        });
        buttons.add(btnLogin);
        btnLogout = new Button("Logout", new ClickListener() {
            public void onClick(final Widget arg0) {
                logout();
            }
        });
        buttons.add(btnLogout);

        final Button btnPanic = new Button("Panic!", new ClickListener() {
            public void onClick(final Widget arg0) {
                panic();
            }
        });
        buttons.add(btnPanic);

        btnSamplesExtUI = new Button("Ext UI Tests", new ClickListener() {
            public void onClick(final Widget sender) {
                chatSamples();
            }
        });
        btnSamplesExtUI.setTitle("Samples in gwt-ext UI (experimental)");
        buttons.add(btnSamplesExtUI);
        return buttons;
    }

    private void createExtUI() {
        dispatcher = DefaultDispatcher.getInstance();
        final PluginManager kunePluginManager = new PluginManager(dispatcher, new UIExtensionPointManager(),
                new I18nTranslationServiceMocked());
        kunePluginManager.install(new ChatDialogPlugin());

        dispatcher.fire(ChatDialogPlugin.OPEN_CHAT_DIALOG,
                new PairChatUser("images/person-def.gif", XmppURI.parse(userNameInput.getText()), userNameInput
                        .getText(), MultiChatView.DEF_USER_COLOR, presenceForTest));

        dispatcher.subscribe(ChatDialogPlugin.ON_STATUS_SELECTED, new Action<Integer>() {
            public void execute(final Integer status) {
                switch (status) {
                case MultiChatView.STATUS_ONLINE:
                    login();
                    break;
                case MultiChatView.STATUS_OFFLINE:
                    logout();
                    break;
                case MultiChatView.STATUS_BUSY:
                    break;
                case MultiChatView.STATUS_INVISIBLE:
                    break;
                case MultiChatView.STATUS_XA:
                    break;
                case MultiChatView.STATUS_AWAY:
                    break;
                default:
                    throw new IndexOutOfBoundsException("Xmpp status unknown");
                }
            }
        });

        dispatcher.subscribe(ChatDialogPlugin.ON_GROUP_CHAT_SUBJECT_CHANGED, new Action<GroupChatSubjectParam>() {
            public void execute(final GroupChatSubjectParam param) {
                Log.info("Group '" + param.getChat() + "' changed subject to '" + param.getSubject()
                        + "' (not implemented yet emite connection");
            }
        });

    }

    private void createInterface() {
        final VerticalPanel vertical = new VerticalPanel();
        vertical.add(createButtonsPane());
        vertical.add(createLoginPane());
        vertical.add(createOutputPane());

        RootPanel.get().add(vertical);
    }

    private HorizontalPanel createLoginPane() {
        final HorizontalPanel login = new HorizontalPanel();
        userNameInput = new TextBox();
        userNameInput.setText("admin@localhost");
        resourceInput = new TextBox();
        resourceInput.setText("emite." + (new Date()).getTime());
        passwordInput = new PasswordTextBox();
        passwordInput.setText("easyeasy");
        login.add(new Label("user name:"));
        login.add(userNameInput);
        login.add(new Label("Resource:"));
        login.add(resourceInput);
        login.add(new Label("password"));
        login.add(passwordInput);
        return login;
    }

    private HorizontalPanel createOutputPane() {
        final HorizontalPanel split = new HorizontalPanel();
        return split;
    }

    private void createPresenceForTest() {
        presenceForTest = new Presence();
        presenceForTest.setShow(Presence.Show.available);
        presenceForTest.setType(Presence.Type.available.toString());
        presenceForTest.setStatus("I\'m out for dinner");
    }

    private void login() {
        xmpp.login(userNameInput.getText(), passwordInput.getText());
        // btnLogin.setEnabled(false);
        // btnLogout.setEnabled(true);
        // dispatcher.fire(ChatDialogPlugin.SET_STATUS,
        // MultiChatView.STATUS_ONLINE);
    }

    private void logout() {
        xmpp.logout();
        btnLogout.setEnabled(false);
        btnLogin.setEnabled(true);
        dispatcher.fire(ChatDialogPlugin.SET_STATUS, MultiChatView.STATUS_OFFLINE);
    }

    private void panic() {
        xmpp.getDispatcher().publish(BoshManager.Events.error);
        btnLogin.setEnabled(true);
        btnLogout.setEnabled(true);

    }

}
