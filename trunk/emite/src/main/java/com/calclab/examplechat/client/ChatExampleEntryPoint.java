package com.calclab.examplechat.client;

import java.util.List;

import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.x.im.chat.MessageListener;
import com.calclab.emite.client.x.im.roster.RosterItem;
import com.calclab.emite.client.x.im.roster.RosterListener;
import com.calclab.emite.client.x.im.session.SessionListener;
import com.calclab.emite.client.x.im.session.Session.State;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.utils.MultiChatSamples;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatExampleEntryPoint implements EntryPoint {
    private Button btnLogin;
    private Button btnLogout;
    private TextBox messageIn;
    private VerticalPanel messageOutput;
    private PasswordTextBox passwordInput;
    private TextBox toIn;
    private TextBox userNameInput;
    private ListBox userSelector;
    private Xmpp xmpp;
    private ScrollPanel messageOutputWrapper;
    private Button btnExtUI;
    private MultiChat extChatDialog;
    private TextBox resourceInput;
    private Button btnSamplesExtUI;

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

        Log.getDivLogger().moveTo(10, 290);

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
        createInterface();

        this.xmpp = Xmpp.create(new BoshOptions("http-bind", "localhost"));

        xmpp.addSessionListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                Log.info("STATE CHANGED: " + current + " - old: " + old);
                switch (current) {
                case connected:
                    btnLogin.setEnabled(false);
                    btnLogout.setEnabled(true);
                    if (extChatDialog != null) {
                        extChatDialog.setStatus(MultiChatView.STATUS_ONLINE);
                    }
                case connecting:
                    btnLogin.setEnabled(false);
                case disconnected:
                    btnLogin.setEnabled(true);
                    btnLogout.setEnabled(false);
                    if (extChatDialog != null) {
                        extChatDialog.setStatus(MultiChatView.STATUS_OFFLINE);
                    }
                }
            }
        });

        xmpp.getRoster().addListener(new RosterListener() {
            public void onRosterChanged(final List<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    Log.info("Rooster, adding: " + item.getName());
                    userSelector.addItem(item.getName(), item.getJid());
                }
            }
        });

        xmpp.addMessageListener(new MessageListener() {
            public void onReceived(final Message message) {
                String text = "\nIN [" + message.getFrom() + "]\n";
                text += message.getBody();
                addMessageToOutput(text);
            }
        });

    }

    private void addMessageToOutput(final String text) {
        messageOutput.add(new Label(text));
        messageOutputWrapper.setScrollPosition(messageOutput.getOffsetHeight());
        // another way (if we are not using ScrollPanels)
        // DOM.setElementPropertyInt(messageOutputWrapper.getElement(),
        // "scrollTop", messageOutput.getOffsetHeight());
    }

    private void chatSamples() {
        MultiChatSamples.show(extChatDialog);
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

        btnExtUI = new Button("Ext UI", new ClickListener() {
            public void onClick(final Widget sender) {
                if (extChatDialog == null) {
                    if (userNameInput.getText().length() == 0 || passwordInput.getText().length() == 0) {
                        Log.warn("Fill user/passwd before");
                    } else {
                        createExtUI();
                    }
                } else {
                    extChatDialog.show();
                }
            }
        });
        btnExtUI.setTitle("gwt-ext UI (experimental)");
        buttons.add(btnExtUI);
        btnSamplesExtUI = new Button("Ext UI Tests", new ClickListener() {
            public void onClick(final Widget sender) {
                if (extChatDialog != null) {
                    chatSamples();
                } else {
                    Log.warn("Please, login and open the ext chat window before");
                }
            }
        });
        btnSamplesExtUI.setTitle("Samples in gwt-ext UI (experimental)");
        buttons.add(btnSamplesExtUI);
        return buttons;
    }

    private void createExtUI() {
        // PluginManager kunePluginManager = new PluginManager(new
        // UIExtensionPointManager(), new I18nTranslationServiceMocked());
        // kunePluginManager.install(new ChatDialogPlugin());

        final AbstractChatUser currentSessionUser = new AbstractChatUser(userNameInput.getText(), userNameInput
                .getText());
        extChatDialog = ChatDialogFactory.createMultiChat(currentSessionUser, new I18nTranslationServiceMocked(),
                new MultiChatListener() {
                    public void onCloseGroupChat(final GroupChat groupChat) {
                    }

                    public void onClosePairChat(final PairChatPresenter pairChat) {
                    }

                    public void onSendMessage(final GroupChat groupChat, final String message) {
                        xmpp.send(toIn.getText(), message);
                    }

                    public void onSendMessage(final PairChatUser toUserChat, final String message) {
                        // xmpp.send(toUserChat.getJid(), message);
                        xmpp.send(toIn.getText(), message);

                    }

                    public void onStatusSelected(final int status) {
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

                    public void onUserColorChanged(final String color) {
                    }

                    public void setGroupChatSubject(final GroupChat groupChat, final String subject) {
                    }
                });
        extChatDialog.show();
    }

    private void createInterface() {
        final VerticalPanel vertical = new VerticalPanel();
        vertical.add(createButtonsPane());
        vertical.add(createLoginPane());
        vertical.add(createMessagePane());
        vertical.add(createOutputPane());

        RootPanel.get().add(vertical);
    }

    private HorizontalPanel createLoginPane() {
        final HorizontalPanel login = new HorizontalPanel();
        userNameInput = new TextBox();
        userNameInput.setText("admin@localhost");
        resourceInput = new TextBox();
        resourceInput.setText("emite");
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

    private VerticalPanel createMessagePane() {
        final VerticalPanel pane = new VerticalPanel();

        final HorizontalPanel controls = new HorizontalPanel();
        pane.add(controls);

        toIn = new TextBox();
        toIn.setText("testuser1@localhost");
        controls.add(toIn);
        messageIn = new TextBox();
        messageIn.setText("hola!");
        messageIn.addKeyboardListener(new KeyboardListener() {
            public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
            }

            public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
                if (keyCode == KeyboardListener.KEY_ENTER) {
                    sendMessageIn();
                }
            }

            public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
            }
        });
        controls.add(messageIn);
        final Button btnSend = new Button("send", new ClickListener() {
            public void onClick(final Widget arg0) {
                sendMessageIn();
            }
        });
        controls.add(btnSend);

        final HorizontalPanel split = new HorizontalPanel();
        userSelector = new ListBox(true);
        userSelector.addClickListener(new ClickListener() {
            public void onClick(final Widget arg0) {
                final String jid = userSelector.getValue(userSelector.getSelectedIndex());
                toIn.setText(jid);
            }
        });
        split.add(userSelector);
        final TabPanel chatTabs = new TabPanel();
        split.add(chatTabs);
        messageOutputWrapper = new ScrollPanel();
        messageOutput = new VerticalPanel();
        messageOutputWrapper.add(messageOutput);
        messageOutputWrapper.setWidth("400");
        messageOutputWrapper.setHeight("200");
        final HorizontalPanel messageOutputWrapper2 = new HorizontalPanel();
        messageOutputWrapper2.add(messageOutputWrapper);
        messageOutputWrapper2.setBorderWidth(1);
        split.add(messageOutputWrapper2);

        pane.add(split);
        split.setHeight("100%");

        return pane;
    }

    private HorizontalPanel createOutputPane() {
        final HorizontalPanel split = new HorizontalPanel();
        return split;
    }

    private void login() {
        xmpp.login(userNameInput.getText(), passwordInput.getText());
        btnLogin.setEnabled(false);
        btnLogout.setEnabled(true);
        if (extChatDialog != null) {
            extChatDialog.setStatus(MultiChatView.STATUS_ONLINE);
        }
    }

    private void logout() {
        xmpp.logout();
        btnLogout.setEnabled(true);
        btnLogin.setEnabled(true);
        if (extChatDialog != null) {
            extChatDialog.setStatus(MultiChatView.STATUS_OFFLINE);
        }
    }

    private void panic() {
        xmpp.getDispatcher().publish(Connection.Events.error);
    }

    private void sendMessageIn() {
        final String msg = messageIn.getText();
        messageIn.setText("");
        addMessageToOutput("sending: " + msg);
        xmpp.send(toIn.getText(), msg);
        messageIn.setFocus(true);
    }
}