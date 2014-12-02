module SessionHelper

	def sessionIsLoggedIn
		return (session[:id] != nil and session[:id] != 0)
	end

	def sessionIsAdmin
		return (sessionIsLoggedIn and (sessionGetUser.permissions == 'super' or sessionGetUser.permissions == 'teacher'))
	end

	def sessionGetUser
		return User.find(session[:id])
	end

	def sessionLogin(user)
		session[:id] = user.id
	end

	def sessionLogout
		session[:id] = nil
	end

	def sessionActiveCheckFailed
		if not sessionIsLoggedIn
			flash[:error] = 'Please sign into Forge first.'
			redirect_to login_path
			return true
		end
		return false
	end

	def sessionAdminCheckFailed
		if not sessionIsLoggedIn or not sessionIsAdmin
			flash[:error] = 'You must be signed into a teacher account to perform this action.'
			redirect_to login_path
			return true
		end
		return false
	end

end