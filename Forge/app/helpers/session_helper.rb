module SessionHelper

	def sessionIsLoggedIn
		return (session[:username] != nil and session[:username] != "")
	end

	def sessionIsAdmin
		return (sessionIsLoggedIn and sessionGetUser.permissions == 'admin')
	end

	def sessionGetUser
		return User.get(session[:username])
	end

	def sessionLogin(user)
		session[:username] = user.key
	end

	def sessionLogout
		session[:username] = nil
	end

	def sessionActiveCheckFailed
		if not sessionIsLoggedIn
			flash[:error] = "Please sign into Forge first."
			redirect_to login_path
			return true
		end
		return false
	end

	def sessionAdminCheckFailed
		if not sessionIsLoggedIn or not sessionIsAdmin
			flash[:error] = "You must be signed into a teacher account to perform this action."
			redirect_to login_path
			return true
		end
		return false
	end

end