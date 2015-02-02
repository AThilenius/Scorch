Rails.application.routes.draw do

  get 'blaze/show'

  get 'flame/show' => 'flame#show'

  get 'minecraft/show'

  get 'students/create'
  get 'students/list'
  get 'students/new'
  get 'account' => 'account#show'
  get 'assignments/create'
  get 'assignments/createLevel'
  get 'assignments/list'
  get 'assignments/new'
  get 'downloads/:id/xcode' => 'downloads#xcode'
  get 'downloads/:id/vs' => 'downloads#vs'
  get 'downloads/:id/makefile' => 'downloads#makefile'
  get 'downloads/flame' => 'downloads#flame'
  get 'login' => 'session#show'
  get 'minecraft_accounts/create'
  get 'minecraft_accounts/list'
  get 'minecraft_accounts/new'
  get 'password' => 'password#show'
  get 'password_change' => 'password#change'
  get 'session_login' => 'session#login'
  get 'session_logout' => 'session#logout'
  post 'service/get_minecraft_session' => 'service#get_minecraft_session'
  post 'service/get_user_level_data' => 'service#get_user_level_data'
  post 'service/get_chat' => 'service#get_chat'
  post 'service/push_chat' => 'service#push_chat'
  match 'blaze/(*path)' => redirect {|params, req| "http://localhost:9886/#{params[:path]}"},  via: [:get, :post]

  resources :minecraft_accounts, :only => [:show, :destroy]
  resources :students, :only => [:show, :destroy]
  resources :assignments, :only => [:show, :destroy]

  root 'assignments#list'
end
