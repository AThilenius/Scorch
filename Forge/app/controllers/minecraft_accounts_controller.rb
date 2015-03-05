class MinecraftAccountsController < ApplicationController

	include SessionHelper

  def list
  	return if sessionAdminCheckFailed

	end

	def destroy
		return if sessionAdminCheckFailed

		@mcAccount = MinecraftAccount.find(params[:id])
		if @mcAccount.nil?
			flash[:error] = "Cannot find find the minecraft account ID: #{params[:id]}"
		else
			flash[:notice] = "Account #{@mcAccount.username} deleted successfully."
			@mcAccount.delete!
		end

		redirect_to minecraft_accounts_list_path
	end

  def show
  	return if sessionAdminCheckFailed

    @mcAccount = MinecraftAccount.find(params[:id])
    if @mcAccount.nil?
      flash[:error] = "Cannot find find the minecraft account ID: #{params[:id]}"
      redirect_to minecraft_accounts_list_path
    end
  end

  def new
  	return if sessionAdminCheckFailed
  end

  def create
  	return if sessionAdminCheckFailed
  	didPass = true
  	failureString = ""

  	if params[:mcpassword].nil? or params[:mcpassword].empty?
			failureString += 'Zero length password. '
			didPass = false
		end

		if params[:mcusername].nil? or params[:mcusername].empty?
			failureString += 'Zero length user name. '
			didPass = false
		end

  	# Check existence
  	if didPass
			# Generate a UUID for the account
			account = MinecraftAccount.create(username: params[:mcusername],
																				password: params[:mcpassword],
																				state: 'free')

			flash[:notice] = "Account #{account.username} Created Successfully."
			redirect_to minecraft_accounts_list_path
  	end
  	
		if not didPass
			flash[:error] = failureString
			redirect_to minecraft_accounts_new_path
		end

  end
end
