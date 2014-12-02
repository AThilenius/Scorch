class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :username,     index: true
      t.string :firstName,    index: true
      t.string :lastName,     index: true
      t.string :studentID,    index: true
      t.string :password
      t.string :permissions,  index: true

      t.timestamps
    end
  end
end
