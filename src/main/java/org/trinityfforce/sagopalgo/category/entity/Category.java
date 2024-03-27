package org.trinityfforce.sagopalgo.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.trinityfforce.sagopalgo.category.dto.request.AddCategoryRequestDto;
import org.trinityfforce.sagopalgo.category.dto.request.ModifyCategoryRequestDto;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Category(AddCategoryRequestDto requestDto) {
        this.name = requestDto.getName();
    }

    public void update(ModifyCategoryRequestDto category) {
        this.name = category.getName() != null ? category.getName() : this.name;
    }
}
