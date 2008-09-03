ActionController::Routing::Routes.draw do |map|
  map.root :controller => 'pages'
  map.connect ':action', :controller => 'pages'
  # map.connect ':controller/:action/:id'
  # map.connect ':controller/:action/:id.:format'
end
