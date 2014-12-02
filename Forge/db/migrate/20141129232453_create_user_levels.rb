class CreateUserLevels < ActiveRecord::Migration
  def change
    create_table :user_levels do |t|
      t.references :user_assignment,    index: true
      t.references :level_description,  index: true
      t.integer :points,                default: 0

      t.timestamps
    end
  end
end
