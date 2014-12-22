Rails.application.routes.draw do

  get 'students/create'
  get 'students/list'
  get 'students/new'
  get 'account' => 'account#show'
  get 'assignments/create'
  get 'assignments/createLevel'
  get 'assignments/download_xcode'
  get 'assignments/list'
  get 'assignments/new'
  get 'blaze' => 'blaze#show'
  get 'login' => 'session#show'
  get 'minecraft_accounts/create'
  get 'minecraft_accounts/list'
  get 'minecraft_accounts/new'
  get 'password' => 'password#show'
  get 'password_change' => 'password#change'
  get 'session_login' => 'session#login'
  get 'session_logout' => 'session#logout'
  post 'service/get_minecraft_session' => 'service#get_minecraft_session'

  resources :minecraft_accounts, :only => [:show, :destroy]
  resources :students, :only => [:show, :destroy]
  resources :assignments, :only => [:show, :destroy]

  #get 'home_page/index'

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  root 'home_page#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
