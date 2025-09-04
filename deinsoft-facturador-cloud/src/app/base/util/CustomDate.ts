import {NgbCalendar, NgbDateAdapter, NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

/**
 * This Service handles how the date is represented in scripts i.e. ngModel.
 */
@Injectable()
export class CustomAdapter extends NgbDateAdapter<string> {

  readonly DELIMITER = '-';

  fromModel(value: string | null): NgbDateStruct | null {
    if (value) {
      let date = value.split(this.DELIMITER);
      return {
        day : parseInt(date[0], 10),
        month : parseInt(date[1], 10),
        year : parseInt(date[2], 10)
      };
    }
    return null;
  }

  toModel(date: NgbDateStruct | null): string | null {
    return date ? this.leftPad(date.day.toString(),2,'0')  + this.DELIMITER + this.leftPad(date.month.toString(),2,'0') + this.DELIMITER + date.year  : null;
  }
  leftPad(str: string, len: number, ch='.'): string {
    len = len - str.length + 1;
    return len > 0 ?
      new Array(len).join(ch) + str : str;
  }
}

/**
 * This Service handles how the date is rendered and parsed from keyboard i.e. in the bound input field.
 */
@Injectable()
export class CustomDateParserFormatter extends NgbDateParserFormatter {

  readonly DELIMITER = '/';

  parse(value: string): NgbDateStruct | null {
    if (value) {
      let date = value.split(this.DELIMITER);
      return {
        day : parseInt(date[0], 10),
        month : parseInt(date[1], 10),
        year : parseInt(date[2], 10)
      };
    }
    return null;
  }

  format(date: NgbDateStruct | null): string {
    return date ? this.leftPad(date.day.toString(),2,'0') + this.DELIMITER + this.leftPad(date.month.toString(),2,'0') + this.DELIMITER + date.year : '';
  }
  leftPad(str: string, len: number, ch='.'): string {
    len = len - str.length + 1;
    return len > 0 ?
      new Array(len).join(ch) + str : str;
  }
  formatDateAndTime(date: Date | null): string {
    return date ? date.getFullYear.toString()
    + this.DELIMITER + this.leftPad(date.getMonth.toString(),2,'0') 
    + this.DELIMITER + this.leftPad(date.getDay.toString(),2,'0') 
    + " " + this.leftPad(date.getHours.toString(),2,'0') 
    + ":" + this.leftPad(date.getMinutes.toString(),2,'0') 
    + ":" + this.leftPad(date.getSeconds.toString(),2,'0') :''
  }
  
}
