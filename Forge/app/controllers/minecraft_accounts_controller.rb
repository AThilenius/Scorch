class MinecraftAccountsController < ApplicationController

	include SessionHelper

  def list
  	return if sessionAdminCheckFailed

	end

	def destroy
		return if sessionAdminCheckFailed

		@mcAccount = MinecraftAccount.get(params[:mcusername])
		if @mcAccount.nil?
			flash[:error] = "Cannot find find the minecraft account: #{params[:mcusername]}"
		else
			@mcAccount.delete
			flash[:notice] = "Account #{@mcAccount.key} deleted successfully."
		end

		redirect_to minecraft_accounts_list_path
	end

  def show
  	return if sessionAdminCheckFailed

    @mcAccount = MinecraftAccount.get(params[:mcusername])
    if @mcAccount.nil?
      flash[:error] = "Cannot find find the minecraft account: #{params[:mcusername]}"
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
			failureString += "Zero length password. "
			didPass = false
		end

		if params[:mcusername].nil? or params[:mcusername].empty?
			failureString += "Zero length user name. "
			didPass = false
		end

  	# Check existence
  	if didPass
  		account = MinecraftAccount.get(params[:mcusername])
  		if account != nil
				failureString += "An account with this user name already exists. "
				didPass = false
			else
				# All good, we can create it
				MinecraftAccount.create(params[:mcusername], params[:mcpassword]).save()
				flash[:notice] = "Account Created Successfully."
				redirect_to minecraft_accounts_list_path
			end
  	end
  	
		if not didPass
			flash[:error] = failureString
			redirect_to minecraft_accounts_new_path
		end

  end
end
