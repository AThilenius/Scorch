Rails.application.routes.draw do

  get 'flame/show'

  get 'minecraft/show'

  get 'students/create'
  get 'students/list'
  get 'students/new'
  get 'account' => 'account#show'
  get 'assignments/create'
  get 'assignments/createLevel'
  get 'assignments/list'
  get 'assignments/new'
  get 'blaze' => 'blaze#show'
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

  resources :minecraft_accounts, :only => [:show, :destroy]
  resources :students, :only => [:show, :destroy]
  resources :assignments, :only => [:show, :destroy]

  root 'assignments#list'
end
