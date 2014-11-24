class SessionController < ApplicationController

	include SessionHelper

	def login
		user = User.fromUsername(params[:username]);

		if user == nil
			flash[:error] = "Wrong user name or password!"
			redirect_to login_path
		else
			if params[:password] != user.password
				flash[:error] = "Wrong user name or password!"
				redirect_to login_path
			else
				#Login sucessful
				sessionLogin(user)
	    	redirect_to root_path
	  	end
		end
	end

	def logout
		sessionLogout
		redirect_to root_path
	end


end
