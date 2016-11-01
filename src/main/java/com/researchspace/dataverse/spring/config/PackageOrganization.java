/*
 * 
 */
package com.researchspace.dataverse.spring.config;
/**
 * /**  Copyright 2016 ResearchSpace

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

*
 * Plant UML documentation  for package dependencies for future refactoring into separate Java binding module.
   @startuml
  skinparam componentStyle uml2
  title Dataverse integration package relations
  
   [external Sword lib] #green
   [spring-rest] #green
   [c.r.d.api1] -up-> [c.r.d.entities / facade]
   note right : model classes for \n JSON objects
   [c.r.d.http] -up-> [c.r.d.api1]
   [c.r.d.http] -up-> [c.r.d.sword]
   [c.r.d.http] --up--> [spring-rest]
   [c.r.d.http] -up-> [c.r.d.springrest.ext]
   [c.r.d.springCfg] -up-> [c.r.d.api1]
   [c.r.d.springCfg] -up-> [c.r.d.http]
   [c.r.d.sword] -up-> [external Sword lib]
    note right: external
    note right of [spring-rest]
     Spring
     end note
  @enduml
 */
public class PackageOrganization {

}
